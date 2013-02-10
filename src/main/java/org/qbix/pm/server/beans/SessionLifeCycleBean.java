package org.qbix.pm.server.beans;

import java.util.Date;

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

import org.qbix.pm.server.dto.SessionInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.intercept.StartSessionEvent;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.polling.PollingParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SessionLifeCycleBean {

	private static Logger log = LoggerFactory
			.getLogger(SessionLifeCycleBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	@Inject
	private Event<StartSessionEvent> startSessionEventBus;

	@EJB
	private SessionFacade sessionFacade;

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
		sess.setStatus(SessionStatus.READY_FOR_POLLING);
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
		} catch (PMException e) {
			log.error(e.getMessage(), e);
		}
	}
}
