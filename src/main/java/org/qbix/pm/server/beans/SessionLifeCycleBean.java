package org.qbix.pm.server.beans;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.annotaions.Traceble;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Traceble
public class SessionLifeCycleBean extends AbstractBean {

	private static Logger log = LoggerFactory
			.getLogger(SessionLifeCycleBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	public void startSessionConfirmation(Session session)
			throws PMLifecycleException {
		if (session.getStatus() == SessionStatus.PLAYERS_CONFIRMATION) {
			// TODO think what to do
		}
		session.setStatus(SessionStatus.PLAYERS_CONFIRMATION);
	}

	public Long registerSession(Session newSession) throws PMException {
		newSession.setStatus(SessionStatus.REGISTERED);
		em.persist(newSession);
		em.flush();
		em.refresh(newSession);

		log.info(String.format("session(id%d) persisted", newSession.getId()));
		return newSession.getId();
	}

	public void confirmParticipation(UserJoinInfo uji) throws PMException {
		//TODO ... updating some entities .. .
	}
}
