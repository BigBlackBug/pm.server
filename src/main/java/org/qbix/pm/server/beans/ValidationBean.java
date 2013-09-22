package org.qbix.pm.server.beans;

import java.math.BigDecimal;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.UserJoinDTO;
import org.qbix.pm.server.exceptions.PMValidationException;
import org.qbix.pm.server.model.Game;
import org.qbix.pm.server.model.GameStatus;
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

	public Game validateGameBeforeRegister(Game game)
			throws PMValidationException {
		notNull(game.getVictoryCriteria(), "game.criteria = null");
		notNull(game.getType(), "game.type = null");
		notNull(game.getStake(), "game.stake = null");

		assertTrue(game.getStake().compareTo(new BigDecimal(0)) != -1,
				"game.stake <= 0");

		game.setID(null);
		
		//TODO teams check
		return game;
	}

	public Game validateGameBeforeUpdating(Game game)
			throws PMValidationException {
		return game;
	}

	public UserJoinDTO validateUserJoinInfoBeforeAdding(UserJoinDTO uji)
			throws PMValidationException {
		notNull(uji.getAccountid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());
		notNull(acc, "No userAccount with id = " + uji.getAccountid());

		Game sess = em.find(Game.class, uji.getSessid());
		
		assertTrue(sess.getStatus() == GameStatus.ACCEPTING_PLAYERS,
				"game status is not 'ACCEPTING_PLAYERS'");

		assertTrue(acc.getBalance().compareTo(sess.getStake()) != -1,
				"user.currency < session.stake");

		return uji;
	}

	public UserJoinDTO validateUserJoinInfoBeforeDisconnecting(UserJoinDTO uji)
			throws PMValidationException {
		notNull(uji.getAccountid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());
		notNull(acc, "No userAccount with id = " + uji.getAccountid());

		//TODO ...
		
		return uji;
	}

	public Game validateGameBeforeStart(Game sess)
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
