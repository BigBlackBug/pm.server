package org.qbix.pm.server.beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.polling.PollingParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Traceable
public class SessionLifeCycleBean extends AbstractBean {

	private static Logger log = LoggerFactory
			.getLogger(SessionLifeCycleBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	@EJB
	private SessionFacade sessionFacade;

	public void startSessionConfirmation(Session session)
			throws PMLifecycleException {
		session.setStatus(SessionStatus.PLAYERS_CONFIRMATION);
	}

	public Long registerSession(Session newSession) throws PMLifecycleException {
		newSession.setStatus(SessionStatus.REGISTERED);
		em.persist(newSession);
		em.flush();
		em.refresh(newSession);

		log.info(String.format("session(id%d) persisted", newSession.getId()));
		return newSession.getId();
	}

	public void confirmParticipation(UserJoinInfo uji)
			throws PMLifecycleException {

		// TODO ...
		/*
		 * if session is ready to start and doesnt require manual start command
		 */
		Session sess = em.find(Session.class, uji.getSessionId());
		startSession(sess);
	}

	public void startSession(Session sess) throws PMLifecycleException {
		sess.setStatus(SessionStatus.POLLING);
		sess.setPollingParams(generatePollingParams(sess));
	}

	private PollingParams generatePollingParams(Session sess) {
		PollingParams pp = new PollingParams();
		pp.setPollerId(1L);
		pp.setSession(sess);
		em.persist(pp);
		return pp;
	}

}
