package org.qbix.pm.server.beans;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMValidationException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.polling.PollingResult;

/**
 * Validates entities/info. <br>
 * 
 * Made @Stateless bean, cause some validations need persistence. <br>
 * All returning <b>entities</b> - managed !
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class ValidationBean {

	public static void notNull(Object obj) throws PMValidationException {
		if (obj == null) {
			throw new PMValidationException("Argument can't be null");
		}
	}

	public static void notNull(Object obj, String mess)
			throws PMValidationException {
		if (obj == null) {
			throw new PMValidationException(mess);
		}
	}

	public static void assertTrue(Boolean bool, String mess)
			throws PMValidationException {
		if (!bool) {
			throw new PMValidationException(mess);
		}
	}

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	public Session validateSessionBeforeRegister(Session sess)
			throws PMValidationException {
		notNull(sess, "session = null");
		notNull(sess.getPlayerRequirements(), "session.playersResolver = null");
		notNull(sess.getVictoryCriteria(), "session.criteria = null");
		notNull(sess.getType(), "session.type = null");

		// TODO playerVal & criteria validations go here ...

		sess.setId(null);
		return sess;
	}

	public Session validateSessionBeforeConfStart(Session sess)
			throws PMValidationException {
		notNull(sess, "session = null");
		notNull(sess.getId(), "session.id = null");

		sess = em.find(Session.class, sess.getId());
		notNull(sess, "No session with id = " + sess.getId());

		return sess;
	}

	public UserJoinInfo validateUserJoinInfo(UserJoinInfo uji)
			throws PMValidationException {
		notNull(uji);
		notNull(uji.getSessid(), "UserJoinInfo.sessid = null");

		Session sess = em.find(Session.class, uji.getSessid());
		notNull(sess, "No session with id = " + uji.getSessid());

		// TODO ...
		return uji;
	}

	public Session validateSessionBeforeStart(Session sess)
			throws PMValidationException {
		notNull(sess, "session = null");
		notNull(sess.getId(), "session.id = null");

		Long sessId = sess.getId();
		sess = em.find(Session.class, sess.getId());
		notNull(sess, "No session with id = " + sessId);

		if (sess.getStatus() != SessionStatus.READY_FOR_POLLING) {
			throw new PMValidationException(String.format(
					"session(id%d) is not in 'READY_FOR_POLLING' status",
					sess.getId()));
		}

		return sess;
	}

	public PollingResult validatePollResultBeforeAnalyzing(PollingResult pr)
			throws PMValidationException {
		notNull(pr);
		assertTrue(pr.isGameFinished(), "game is not finished yet");

		return pr;
	}
}
