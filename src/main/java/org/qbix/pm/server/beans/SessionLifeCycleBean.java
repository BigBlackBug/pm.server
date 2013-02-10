package org.qbix.pm.server.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.dto.SessionInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.intercept.ResultReadyEvent;
import org.qbix.pm.server.intercept.StartSessionEvent;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.UserAccount;
import org.qbix.pm.server.money.MoneyTransferBean;
import org.qbix.pm.server.money.SimpleMoneyTransferInfo;
import org.qbix.pm.server.polling.PollingParams;
import org.qbix.pm.server.polling.PollingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SessionLifeCycleBean extends AbstractBean {

	private static Logger log = LoggerFactory
			.getLogger(SessionLifeCycleBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	@Inject
	private Event<StartSessionEvent> startSessionEventBus;

	@EJB
	private SessionFacade sessionFacade;

	@EJB
	private MoneyTransferBean moneyTransfer;

	public void startSessionConfirmation(Session session)
			throws PMLifecycleException {
		session.setStatus(SessionStatus.ACCEPTING_PLAYERS);
	}

	public Long registerSession(Session newSession) throws PMLifecycleException {
		newSession.setStatus(SessionStatus.REGISTERED);
		newSession.setCreationDate(new Date());
		em.persist(newSession);
		em.flush();
		em.refresh(newSession);

		log.info(String.format("session(id%d) registered", newSession.getId()));
		return newSession.getId();
	}

	public void confirmParticipation(UserJoinInfo uji)
			throws PMLifecycleException {

		Session sess = em.find(Session.class, uji.getSessid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());

		sess.setStatus(SessionStatus.READY_FOR_POLLING);
		moneyTransfer.userSessionParticipation(sess, acc, sess.getStake()
				.negate());

		PlayerEntry pe = new PlayerEntry();
		pe.setSession(sess);
		pe.setAccount(acc);
		pe.setTeam(SessionTeam.valueOf(uji.getTeam()));
		pe.setStake(sess.getStake());
		em.persist(pe);

		/*
		 * if session is ready to start and doesnt require manual start command
		 */
		// TODO --MOCK--
		if (true) {
			startSessionEventBus.fire(new StartSessionEvent(sess.getId()));
		}
	}

	public void startSession(Session sess) throws PMLifecycleException {
		log.info(String.format("enabling session(id%d) polling", sess.getId()));
		sess.setStatus(SessionStatus.POLLING);
		sess.setPollingParams(generatePollingParams(sess));
		sess.setSessionStartDate(new Date());
		em.persist(sess);
		em.flush();
	}

	private PollingParams generatePollingParams(Session sess) {
		PollingParams pp = new PollingParams();
		pp.setPollerId(1L);
		pp.setSession(sess);
		em.persist(pp);
		return pp;
	}

	public void resolveStartSessionEvent(
			@Observes(during = TransactionPhase.AFTER_SUCCESS) StartSessionEvent sse) {
		try {
			sessionFacade.startSession(new SessionInfo(sse.sessionId));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Asynchronous
	@Traceable
	public void processResultReadyEvent(
			@Observes(during = TransactionPhase.AFTER_SUCCESS) ResultReadyEvent rre) {
		try {
			SimpleMoneyTransferInfo mti = getMoneyTransferInfo(rre.resultId);
			moneyTransfer.transfer(mti);

			Session sess = em.find(Session.class, rre.resultId);
			sess.setStatus(SessionStatus.CLOSED);
			log.info("session's(id%d) result resolved, session closed");

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private SimpleMoneyTransferInfo getMoneyTransferInfo(Long pollingResultId) {
		PollingResult pr = em.find(PollingResult.class, pollingResultId);
		Session sess = pr.getSession();

		log.info(String.format("analyzing session(id%s) result", sess.getId()));

		// TODO brute force! add resolving, depending on PollingResult.paramsMap
		SessionTeam winnerTeam = pr.getWinnerTeam();
		List<Long> winners = new ArrayList<Long>();
		BigDecimal winnersMoney = new BigDecimal(0);
		for (PlayerEntry pe : sess.getPlayers()) {
			winnersMoney.add(pe.getStake());

			if (pe.getTeam().equals(winnerTeam)) {
				winners.add(pe.getAccount().getId());
			}
		}

		BigDecimal moneyFor1Winner = winnersMoney.divide(new BigDecimal(winners
				.size()));

		SimpleMoneyTransferInfo mti = new SimpleMoneyTransferInfo(sess.getId());

		for (Long winnerAccId : winners) {
			mti.getTransferDetails().put(winnerAccId, moneyFor1Winner);
		}

		return mti;
	}
}
