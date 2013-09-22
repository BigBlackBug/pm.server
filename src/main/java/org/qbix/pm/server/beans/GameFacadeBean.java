package org.qbix.pm.server.beans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.annotaions.Traceable.LogLevel;
import org.qbix.pm.server.dto.Notification;
import org.qbix.pm.server.dto.NotificationType;
import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.GameDTO;
import org.qbix.pm.server.dto.UserJoinDTO;
import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Game;
import org.qbix.pm.server.model.UserAccount;

import com.google.gson.Gson;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Traceable(logLevel = LogLevel.DEBUG)
public class GameFacadeBean extends AbstractBean implements GameFacade,
		ClientAPI {

	@EJB
	private ValidationBean validationBean;

	@EJB
	private GameLifeCycleBean lifecycleBean;

	@EJB
	private NotificationBean notifier;

	@Override
	public Long registerGame(GameDTO si) throws PMException {
		Game newGame = si.convertToEntity(em);
		newGame = validationBean.validateGameBeforeRegister(newGame);
		return lifecycleBean.registerGame(newGame);
	}

	@Override
	public Long updateGame(GameDTO si) throws PMException {
		Game update = si.convertToEntity(em);
		lockEntity(Game.class, si.getID());
		update = validationBean.validateGameBeforeUpdating(update);
		lifecycleBean.updateGame(update);
		Gson gson = new Gson();
		String json = gson.toJson(si);
		notifier.notifyWithJMS(new Notification(
				NotificationType.GAME_PARAMETERS_CHANGED,
				getAccountIds(update), json));
		return update.getID();
	}

	@Override
	public void playerDisconnected(UserJoinDTO uji) throws PMException {
		Game game = lockEntity(Game.class, uji.getSessid());
		uji = validationBean.validateUserJoinInfoBeforeDisconnecting(uji);
		lifecycleBean.playerDisconnected(uji);
		notifier.notifyWithJMS(new Notification(
				NotificationType.PLAYER_DISCONNECTED, getAccountIds(game),
				"account_id", uji.getAccountid()));
	}

	@Override
	public void confirmParticipation(UserJoinDTO uji) throws PMException {
		Game game = lockEntity(Game.class, uji.getSessid());
		uji = validationBean.validateUserJoinInfoBeforeAdding(uji);
		lifecycleBean.confirmParticipation(uji);
		notifier.notifyWithJMS(new Notification(
				NotificationType.PLAYER_CONFIRMED_STAKE, getAccountIds(game),
				"account_id", uji.getAccountid()));
	}

	@Override
	public void cancelParticipation(UserJoinDTO uji) throws PMException {
		Game game = lockEntity(Game.class, uji.getSessid());
		uji = validationBean.validateUserJoinInfoBeforeDisconnecting(uji);
		lifecycleBean.cancelParticipation(uji);

		notifier.notifyWithJMS(new Notification(
				NotificationType.PLAYER_CANCELLED_STAKE, getAccountIds(game),
				"account_id", uji.getAccountid()));
	}

	@Override
	public void startGame(GameDTO si) throws PMException {
		Game game = si.convertToEntity(em);
		lockEntity(Game.class, si.getID());
		game = validationBean.validateGameBeforeStart(game);
		lifecycleBean.startGame(game);
		notifier.notifyWithJMS(new Notification(NotificationType.GAME_STARTED,
				getAccountIds(game)));
	}

	@Override
	public void resolveResult(ResultInfo ri) throws PMException {
		lockEntity(Game.class, ri.getGameID());
		ri = validationBean.validateResult(ri);
		lifecycleBean.resolveResultAndCloseSession(ri);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Map<Long, Long> getPmIdByLolId(List<Long> ids) throws PMException {
		if (ids == null || ids.isEmpty()) {
			return Collections.emptyMap();
		}

		String query = "SELECT userAcc.ID, userAcc.lolAccount.accountID FROM "
				+ UserAccount.class.getName()
				+ " userAcc WHERE userAcc.lolAccount.accountID IN ( :ids )";

		List<Object[]> tuples = em.createQuery(query, Object[].class)
				.setParameter("ids", ids).getResultList();

		Map<Long, Long> result = new HashMap<>();
		for (Object[] tuple : tuples) {
			Long pmId = (Long) tuple[0];
			Long lolId = (Long) tuple[1];
			result.put(lolId, pmId);
		}
		return result;
	}

	private List<Long> getAccountIds(Game game) {
		List<Long> ids = new ArrayList<>();
		for (PlayerEntry pe : game.getPlayers()) {
			ids.add(pe.getAccount().getID());
		}
		return ids;
	}

}
