package org.qbix.pm.server.beans;

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
import org.qbix.pm.server.util.Utils;

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

	public static void fail(String message) throws PMValidationException {
		throw new PMValidationException(message);
	}

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	public Game validateGameBeforeRegister(Game game)
			throws PMValidationException {
		notNull(game.getVictoryCriteria(), "game.criteria = null");
		notNull(game.getType(), "game.type = null");
		notNull(game.getStake(), "game.stake = null");

		assertTrue(Utils.lt0(game.getStake()), "game.stake <= 0");

		game.setID(null);
		return game;
	}

	public Game validateGameBeforeUpdating(Game game)
			throws PMValidationException {
		// TODO game status check
		return game;
	}

	public UserJoinDTO validateUserJoinInfoBeforeAdding(UserJoinDTO uji)
			throws PMValidationException {
		notNull(uji.getAccountid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());
		notNull(acc, "No userAccount with id = " + uji.getAccountid());

		Game game = em.find(Game.class, uji.getGameId());

		assertTrue(game.getStatus() == GameStatus.ACCEPTING_PLAYERS,
				"game status is not 'ACCEPTING_PLAYERS'");

		assertTrue(acc.getBalance().compareTo(game.getStake()) != -1,
				"user.currency < session.stake");

		return uji;
	}

	public UserJoinDTO validateUserJoinInfoBeforeDisconnecting(UserJoinDTO uji)
			throws PMValidationException {
		notNull(uji.getAccountid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());
		notNull(acc, "No userAccount with id = " + uji.getAccountid());

		// TODO game status check

		return uji;
	}

	public Game validateGameBeforeStart(Game game) throws PMValidationException {
		assertTrue(game.getStatus() == GameStatus.READY_TO_START,
				"game.status is not READY_TO_START");
		return game;
	}

	public ResultInfo validateResult(ResultInfo ri)
			throws PMValidationException {
		notNull(ri);
		return ri;
	}

	public Game validateGameBeforeCancelling(Game game)
			throws PMValidationException {
		GameStatus status = game.getStatus();

		switch (status) {
		case DOES_NOT_EXIST:
			break;
		case ACCEPTING_PLAYERS:
			break;
		case READY_TO_START:
			break;
		default:
			fail("can't cancel game in status " + status);
		}

		return game;
	}

}
