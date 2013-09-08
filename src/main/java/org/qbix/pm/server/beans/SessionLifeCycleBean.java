package org.qbix.pm.server.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.SessionType;
import org.qbix.pm.server.model.UserAccount;
import org.qbix.pm.server.model.VictoryCriteria;
import org.qbix.pm.server.money.MoneyTransferBean;
import org.qbix.pm.server.money.SimpleMoneyTransferInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SessionLifeCycleBean extends AbstractBean {

	private static Logger log = LoggerFactory
			.getLogger(SessionLifeCycleBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	@EJB
	private SessionFacade sessionFacade;

	@EJB
	private MoneyTransferBean moneyTransfer;
	
	public Long registerSession(Session newSession) {
		newSession.setStatus(SessionStatus.ACCEPTING_PLAYERS);
		em.persist(newSession);
		em.flush();
		em.refresh(newSession);

		log.info(String.format("session(id%d) registered", newSession.getId()));
		return newSession.getId();
	}
	
	public Long updateSession(Session session) {
		Session managedSession = em.getReference(Session.class, session.getId());
		
		BigDecimal stake = session.getStake();
		if (stake != null) {
			managedSession.setStake(stake);
		}

		VictoryCriteria vc = session.getVictoryCriteria();
		if (vc != null) {
			managedSession.setVictoryCriteria(vc);
		}

		SessionType type = session.getType();
		if (type != null) {
			managedSession.setType(type);
		}

		em.flush();

		log.info(String.format("session(id%d) updated.", session.getId()));
		
		return session.getId();
	}
	

	public Long updateParicipants(Session session) {
		Session managedSession = em.getReference(Session.class, session.getId());
		
		Set<PlayerEntry> managedPlayers = managedSession.getPlayers();
		managedPlayers.addAll(session.getPlayers());
		
		em.flush();
		
		log.info(String.format("session(id%d) updated participants.",
				session.getId()));
		
		return session.getId();
	}
	
	public void playerDisconnected(UserJoinInfo uji) {
		Session sess = em.find(Session.class, uji.getSessid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());

		Iterator <PlayerEntry> it = sess.getPlayers().iterator();
		while(it.hasNext()){
			PlayerEntry next = it.next();
			if(next.getAccount().equals(acc)){
				it.remove();
				break;
			}
		}
		log.info(String.format(
				"user(id%d) disconnected from session(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	public void confirmParticipation(UserJoinInfo uji) {

		Session sess = em.find(Session.class, uji.getSessid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());

		for (PlayerEntry pe : sess.getPlayers()) {
			if (pe.getAccount().equals(acc)) {
				pe.setStake(sess.getStake());
				break;
			}
		}

		if (checkIsSessionIsReadyToStart(sess)) {
			sess.setStatus(SessionStatus.READY_TO_START);
		}
		
		em.flush();
		
		log.info(String.format(
				"user(id%d) confirmed participation is session(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	public void cancelParticipation(UserJoinInfo uji) {
		for (PlayerEntry pe : em.getReference(Session.class, uji.getSessid())
				.getPlayers()) {
			if (pe.getAccount().getId().equals(uji.getAccountid())) {
				pe.setStake(new BigDecimal(-1));
				break;
			}
		}
		
		em.flush();
		
		log.info(String.format(
				"user(id%d) cancelled participation is session(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	private boolean checkIsSessionIsReadyToStart(Session sess) {
		// check status required ???
		for (PlayerEntry pe : sess.getPlayers()) {
			if (pe.getStake().doubleValue() < 0) {
				return false;
			}
		}

		return true;
	}

	public void startSession(Session sess) throws PMLifecycleException {
		sess.setStatus(SessionStatus.SESSION_COMMITED);
		em.persist(sess);
		em.flush();
		log.info("session(id%d) started");
	}

	public void resolveResultAndCloseSession(ResultInfo ri)
			throws PMLifecycleException {
		SimpleMoneyTransferInfo mti = getMoneyTransferInfo(ri);
		moneyTransfer.transfer(mti);

		em.find(Session.class, ri.getSessid()).setStatus(SessionStatus.CLOSED);
		log.info("session's(id%d) result resolved, session closed");
	}

	private SimpleMoneyTransferInfo getMoneyTransferInfo(ResultInfo ri) {
		log.info(String.format("analyzing session(id%s) result", ri.getSessid()));

		Session sess = em.find(Session.class,ri.getSessid());
		SessionTeam winnerTeam = SessionTeam.valueOf(ri.getWinner());
		List<Long> winners = new ArrayList<Long>();
		BigDecimal winnersMoney = new BigDecimal(0);
		for (PlayerEntry pe : sess.getPlayers()) {
			// fucking BigDecimal!
			winnersMoney = new BigDecimal(winnersMoney.doubleValue()
					+ pe.getStake().doubleValue());

			if (pe.getTeam().getCode() == winnerTeam.getCode()) {
				winners.add(pe.getAccount().getId());
			}
		}

		BigDecimal moneyFor1Winner = winnersMoney.divide(
				new BigDecimal(winners.size()), 2);

		SimpleMoneyTransferInfo mti = new SimpleMoneyTransferInfo(
				sess.getId());

		for (Long winnerAccId : winners) {
			mti.getTransferDetails().put(winnerAccId, moneyFor1Winner);
		}

		return mti;
	}

}
