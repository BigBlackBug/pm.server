package org.qbix.pm.server.polling;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.intercept.ResultReadyEvent;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.util.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Asynchronous
public class PollingBean {

	private static final Logger log = LoggerFactory
			.getLogger(PollingBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	@Inject
	private Event<ResultReadyEvent> resultReady;

	public void poll(Long sessionId) {
		Session session = em.find(Session.class, sessionId);

		log.debug("polling session(id" + session.getId() + ")");

		PollingParams pollingParams = session.getPollingParams();
		AbstractPoller<PollingResult, PollingParams> p = Cache
				.getPoller(pollingParams.getPollerId());

		try {
			PollingResult result = p.poll(pollingParams);

			PollingLogEntry logEntry = new PollingLogEntry();
			logEntry.setJsonParams(result.getJsonParams());
			logEntry.setReturnCode(result.getReturnCode());
			logEntry.setTimestamp(result.getTimestamp());
			em.persist(logEntry);

			if (result.isGameFinished()) {
				session.setStatus(SessionStatus.ANALYZING_RESULT);
				em.persist(result);
				em.flush();
				em.refresh(result);
				resultReady.fire(new ResultReadyEvent(result.getId()));
			}

			log.debug("polling session(id" + session.getId() + ") end");

		} catch (PMPollingException e) {
			log.warn(e.getMessage());
			// TODO persisted message
		}
	}

	public void resolveResult(
			@Observes(during = TransactionPhase.AFTER_SUCCESS) ResultReadyEvent rre) {
		log.info("resolving result");
		PollingResult pr = em.find(PollingResult.class, rre.resultId);
		pr.getSession().setStatus(SessionStatus.PREPARING_TO_TRANSFER);
	}
}
