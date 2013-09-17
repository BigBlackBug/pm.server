package org.qbix.pm.server.beans;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.dto.ParticipantsInfo;
import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMValidationException;
import org.qbix.pm.server.model.LoLAccount;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.UserAccount;

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
	
	public static void fail(String message)
			throws PMValidationException {
		throw new PMValidationException(message);
	}

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	public Session validateSessionBeforeRegister(Session sess)
			throws PMValidationException {
		notNull(sess.getVictoryCriteria(), "session.criteria = null");
		notNull(sess.getType(), "session.type = null");
		notNull(sess.getStake(), "session.stake = null");

		assertTrue(sess.getStake().compareTo(new BigDecimal(0)) != -1,
				"session.stake <= 0");

		sess.setId(null);
		return sess;
	}

	public Session validateSessionBeforeUpdating(Session sess)
			throws PMValidationException {
		BigDecimal stake = sess.getStake();
		if (stake != null) {
			assertTrue(stake.compareTo(new BigDecimal(0)) != -1,
					"session.stake <= 0");
		}
		return sess;
	}

	public ParticipantsInfo validateParticipantsInfo(ParticipantsInfo info)
			throws PMValidationException {
		List<Long> team0 = info.getTeamOne();
		validateTeam(team0);
		List<Long> team1 = info.getTeamTwo();
		validateTeam(team1);
		return info;
	}
	
	private void validateTeam(List<Long> team) throws PMValidationException {
		for (Long lolAccountID : team) {
			try {
				em.createQuery(
						"select acc from LoLAccount acc where acc.accountID = :id",
						LoLAccount.class).setParameter("id", lolAccountID)
						.getSingleResult();
			} catch (NoResultException ex) {
				fail(String.format("no account with lolaccountid = %d",
						lolAccountID));
			}
		}
	}

	public UserJoinInfo validateUserJoinInfoBeforeAdding(UserJoinInfo uji)
			throws PMValidationException {
		notNull(uji.getAccountid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());
		notNull(acc, "No userAccount with id = " + uji.getAccountid());

		Session sess = em.find(Session.class, uji.getSessid());
		
		// TODO check correсtly!
		assertTrue(acc.getBalance().compareTo(sess.getStake()) != -1,
				"user.currency < session.stake");

		return uji;
	}

	public UserJoinInfo validateUserJoinInfoBeforeDisconnecting(UserJoinInfo uji)
			throws PMValidationException {
		notNull(uji.getAccountid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());
		notNull(acc, "No userAccount with id = " + uji.getAccountid());

		return uji;
	}

	public Session validateSessionBeforeStart(Session sess)
			throws PMValidationException {
		// assertTrue(sess.getStatus() == SessionStatus.READY_FOR_POLLING,
		// String.format("session(id%d).status != 'READY_FOR_POLLING'",
		// sessId));

		return sess;
	}

	public ResultInfo validateResult(ResultInfo ri)
			throws PMValidationException {
		notNull(ri);
		return ri;
	}

}
