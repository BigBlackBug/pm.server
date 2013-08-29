package org.qbix.pm.server.beans;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMValidationException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.PlayerRequirements;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.UserAccount;
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
		notNull(sess.getVictoryCriteria(), "session.criteria = null");
		notNull(sess.getType(), "session.type = null");
		notNull(sess.getStake(), "session.stake = null");

		assertTrue(sess.getStake().compareTo(new BigDecimal(0)) != -1,
				"session.stake <= 0");

		validatePlayerRequirements(sess.getPlayerRequirements());

		// TODO criteria validations go here ...

		sess.setId(null);
		return sess;
	}

	private void validatePlayerRequirements(PlayerRequirements reqs)
			throws PMValidationException {
		notNull(reqs, "session.playerRequirements = null");

		// Map<String, Object> params = reqs.getParamsMap();
		// Object playersIds = params
		// .get(PlayerRequirementsInfo.PLAYERS_IDS_PROP_NAME);

		// notNull(playersIds,
		// "PlayerRequirements don't contain players ids list");
		// assertTrue(playersIds instanceof List,
		// "PlayerRequirements don't contain players ids LIST");

		// TODO validation here ...
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
		notNull(uji.getSessid(), "userJoinInfo.sessid = null");

		notNull(uji.getTeam(), "userJoinInfo.team = null");
		notNull(SessionTeam.valueOf(uji.getTeam()), "userJoinInfo.team != 0/1");

		Session sess = em.find(Session.class, uji.getSessid());
		notNull(sess, "No session with id = " + uji.getSessid());

		notNull(uji.getAccountid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());
		notNull(acc, "No userAccount with id = " + uji.getAccountid());

		assertTrue(acc.getCurrency().compareTo(sess.getStake()) != -1,
				"user.currency < session.stake");

		for (PlayerEntry pe : sess.getPlayers()) {
			assertTrue(!pe.getAccount().getId().equals(uji.getAccountid()),
					"session already contains this player");
		}

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

		assertTrue(sess.getStatus() == SessionStatus.READY_FOR_POLLING,
				String.format("session(id%d).status != 'READY_FOR_POLLING'",
						sessId));

		return sess;
	}

	public PollingResult validatePollResultBeforeResolving(Long resultId)
			throws PMValidationException {
		notNull(resultId);

		PollingResult pr = em.find(PollingResult.class, resultId);
		notNull(pr, "pollingResult = null");
		assertTrue(pr.isGameFinished(), "game is not finished yet");

		assertTrue(pr.getSession().getStatus() == SessionStatus.RESULT_READY,
				"polling result is not ready");

		return pr;
	}
}
