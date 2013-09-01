package org.qbix.pm.server.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import org.qbix.pm.server.model.UserAccount;
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

	public Long registerSession(Session newSession) throws PMLifecycleException {
		newSession.setStatus(SessionStatus.ACCEPTING_PLAYERS);
		em.persist(newSession);
		em.flush();
		em.refresh(newSession);

		log.info(String.format("session(id%d) registered", newSession.getId()));
		// TODO notify all players
		return newSession.getId();
	}

	public void confirmParticipation(UserJoinInfo uji)
			throws PMLifecycleException {

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

		// TODO notify all
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
		log.info(String.format("enabling session(id%d) polling", sess.getId()));
		sess.setStatus(SessionStatus.SESSION_COMMITED);
		em.persist(sess);
		em.flush();

		// TODO notify
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
