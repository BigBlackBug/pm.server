package org.qbix.pm.server.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.UserJoinDTO;
import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.model.Game;
import org.qbix.pm.server.model.GameStatus;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Team;
import org.qbix.pm.server.model.UserAccount;
import org.qbix.pm.server.model.VictoryCriteria;
import org.qbix.pm.server.money.MoneyTransferBean;
import org.qbix.pm.server.money.SimpleMoneyTransferInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class GameLifeCycleBean extends AbstractBean {

	private static Logger log = LoggerFactory
			.getLogger(GameLifeCycleBean.class);

	@EJB
	private GameFacade gameFacade;

	@EJB
	private MoneyTransferBean moneyTransfer;

	public Long registerGame(Game game) {
		game.setStatus(GameStatus.ACCEPTING_PLAYERS);
		em.persist(game);
		em.flush();
		em.refresh(game);

		log.info(String.format("game(id%d) registered", game.getID()));
		return game.getID();
	}

	public Game updateGame(Game game) {
		Game managedGame = em.find(Game.class, game.getID());

		if (game.getPlayers() != null) {
			updatePlayers(game.getID(), managedGame,
					game);
		}
		
		VictoryCriteria vc = game.getVictoryCriteria();
		if (vc != null) {
			managedGame.setVictoryCriteria(vc);
		}

		BigDecimal stake = game.getStake();
		if (stake != null) {
			managedGame.setStake(stake);
		}
		
		managedGame.setStatus(GameStatus.ACCEPTING_PLAYERS);
		
		log.info(String.format("game(id%d) updated.", game.getID()));

		return managedGame;
	}

	private void updatePlayers(Long gameId, Game oldGame,
			Game newGame) {
		moneyTransfer.refreshPlayersList(oldGame);
		oldGame.getPlayers().clear();
		oldGame.getPlayers().addAll(newGame.getPlayers());
	}

	public void playerDisconnected(UserJoinDTO uji) {
		Game game = em.find(Game.class, uji.getSessid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());

		Iterator<PlayerEntry> it = game.getPlayers().iterator();
		while (it.hasNext()) {
			PlayerEntry next = it.next();
			if (next.getAccount().equals(acc)) {
				it.remove();
				break;
			}
		}
		log.info(String.format("user(id%d) disconnected from game(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	public void confirmParticipation(UserJoinDTO uji) {
		Game game = em.find(Game.class, uji.getSessid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());

		for (PlayerEntry pe : game.getPlayers()) {
			if (pe.getAccount().equals(acc)) {
				pe.setStake(game.getStake());
				break;
			}
		}

		moneyTransfer.userGameParticipation(game, acc);

		if (checkIsSessionIsReadyToStart(game)) {
			game.setStatus(GameStatus.READY_TO_START);
		}

		log.info(String.format(
				"user(id%d) confirmed participation is game(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	public void cancelParticipation(UserJoinDTO uji) {
		Game game = em.find(Game.class, uji.getSessid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());
		for (PlayerEntry pe : game.getPlayers()) {
			if (pe.getAccount().getID().equals(uji.getAccountid())) {
				pe.setStake(new BigDecimal(-1));
				break;
			}
		}

		moneyTransfer.calcelUserGameParticipation(game, acc);

		log.info(String.format(
				"user(id%d) cancelled participation is game(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	private boolean checkIsSessionIsReadyToStart(Game sess) {
		// check status required ???
		for (PlayerEntry pe : sess.getPlayers()) {
			if (pe.getStake().doubleValue() < 0) {
				return false;
			}
		}

		return true;
	}

	public void startGame(Game game) throws PMLifecycleException {
		game = em.find(Game.class, game.getID());
		game.setStatus(GameStatus.COMMITED);
		log.info(String.format("game(id%d) started", game.getID()));
	}

	public void resolveResultAndCloseSession(ResultInfo ri)
			throws PMLifecycleException {
		SimpleMoneyTransferInfo mti = getMoneyTransferInfo(ri);
		moneyTransfer.transfer(mti);

		em.find(Game.class, ri.getGameID()).setStatus(GameStatus.CLOSED);
		log.info("game(id%d) result resolved, game closed");
	}

	private SimpleMoneyTransferInfo getMoneyTransferInfo(ResultInfo ri) {
		log.info(String.format("analyzing session(id%s) result", ri.getGameID()));

		Game game = em.find(Game.class, ri.getGameID());
		Team winnerTeam = Team.valueOf(ri.getWinner());
		List<Long> winners = new ArrayList<Long>();
		BigDecimal winnersMoney = new BigDecimal(0);
		for (PlayerEntry pe : game.getPlayers()) {
			// fucking BigDecimal!
			winnersMoney = new BigDecimal(winnersMoney.doubleValue()
					+ pe.getStake().doubleValue());

			if (pe.getTeam().getCode() == winnerTeam.getCode()) {
				winners.add(pe.getAccount().getID());
			}
		}

		BigDecimal moneyFor1Winner = winnersMoney.divide(
				new BigDecimal(winners.size()), 2);

		SimpleMoneyTransferInfo mti = new SimpleMoneyTransferInfo(game.getID());

		for (Long winnerAccId : winners) {
			mti.getTransferDetails().put(winnerAccId, moneyFor1Winner);
		}

		return mti;
	}

}
