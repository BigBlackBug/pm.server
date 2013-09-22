package org.qbix.pm.server.money;

import java.math.BigDecimal;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.beans.AbstractBean;
import org.qbix.pm.server.exceptions.PMTransferMoneyException;
import org.qbix.pm.server.model.Game;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.UserAccount;
import org.qbix.pm.server.money.MoneyTransferLogEntry.MoneyTransferAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Traceable
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MoneyTransferBean extends AbstractBean {

	private static final Logger log = LoggerFactory
			.getLogger(MoneyTransferBean.class);

	public void transfer(SimpleMoneyTransferInfo mti)
			throws PMTransferMoneyException {

		log.info(String.format("transfering session(id%d) result's money",
				mti.getGameId()));

		// TODO need validation here ??
		Game game = em.find(Game.class, mti.getGameId());

		for (Long acc : mti.getTransferDetails().keySet()) {
			UserAccount ua = lockEntity(UserAccount.class, acc);
			addToUserAcc(ua, mti.getTransferDetails().get(acc),
					MoneyTransferAction.GAME_RESULT_RESOLVING, game);
		}
	}

	public void userGameParticipation(Game game, UserAccount userAcc) {
		userAcc = lockEntity(UserAccount.class, userAcc.getID());
		BigDecimal stake = game.getStake();
		userAcc.setInGameCash(stake);
		stake = stake.negate();
		addToUserAcc(userAcc, stake,
				MoneyTransferAction.GAME_PLAYER_PARTICIPATION, game);
	}

	public void calcelUserGameParticipation(Game game, UserAccount userAcc) {
		userAcc = lockEntity(UserAccount.class, userAcc.getID());
		BigDecimal stake = game.getStake();
		userAcc.setInGameCash(new BigDecimal(0));
		addToUserAcc(userAcc, stake,
				MoneyTransferAction.CANCEL_GAME_PLAYER_PARTICIPATION, game);
	}

	public void refreshPlayersList(Game game) {
		BigDecimal oldStake = game.getStake();
		for (PlayerEntry pe : game.getPlayers()) {
			UserAccount acc = lockEntity(UserAccount.class, pe.getAccount()
					.getID());
			
			/* если чел уже успел согласится */
			if(!pe.getStake().equals(new BigDecimal(-1))){
				pe.setStake(new BigDecimal(-1));
				
				acc.setInGameCash(new BigDecimal(0));
				
				addToUserAcc(acc, oldStake, MoneyTransferAction.REFRESH_PLAYERS_PARTICIPATION, game);
			}
		}
	}

	/** userAcc entity should be locked */
	private void addToUserAcc(UserAccount account, BigDecimal money,
			MoneyTransferAction action, Game game) {

		account.setBalance(account.getBalance().add(money));

		MoneyTransferLogEntry mtle = new MoneyTransferLogEntry();
		mtle.setTargetAccount(account);
		mtle.setCurrency(money);
		mtle.setAction(action);
		mtle.setGame(game);
		em.persist(mtle);
	}

}
