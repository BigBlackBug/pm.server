package org.qbix.pm.server.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMValidationException;
import org.qbix.pm.server.model.Session;

/**
 * Validates entities/info. <br>
 * 
 * Made @Stateless bean, cause some validations need persistence. <br>
 * All returning <b>entities</b> - managed !
 */
@Stateless
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

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	public Session validateSessionBeforeRegister(Session sess)
			throws PMValidationException {
		notNull(sess, "session = null");
		notNull(sess.getPlayersValidation(), "playersResolver = null");
		notNull(sess.getResolveResultCriteria(), "criteria = null");

		// TODO playerVal & criteria validations go here ...

		sess.setId(null);
		return sess;
	}

	public Session validateSessionBeforeConfStart(Session sess)
			throws PMValidationException {
		notNull(sess, "session = null");
		notNull(sess.getId(), "session.id = null");

		sess = em.find(Session.class, sess.getId());
		notNull(sess, "No session with this id");

		return sess;
	}

	public UserJoinInfo validateUserJoinInfo(UserJoinInfo uji)
			throws PMValidationException {
		notNull(uji);
		notNull(uji.getSessid(), "sessid = null");
		// TODO ...
		return uji;
	}

}
