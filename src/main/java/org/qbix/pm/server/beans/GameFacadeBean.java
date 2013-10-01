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
import org.qbix.pm.server.dto.GameDTO;
import org.qbix.pm.server.dto.Notification;
import org.qbix.pm.server.dto.NotificationType;
import org.qbix.pm.server.dto.PlayerEntryDTO;
import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.UserJoinDTO;
import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.interfaces.ClientAPI;
import org.qbix.pm.server.interfaces.GameFacade;
import org.qbix.pm.server.model.Game;
import org.qbix.pm.server.model.PlayerEntry;
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
	
	private final Gson gson;
	
	public GameFacadeBean() {
		gson = new Gson();
	}

	@Override
	public Long registerGame(GameDTO gameDTO) throws PMException {
		Game newGame = gameDTO.convertToEntity(em);
		newGame = validationBean.validateGameBeforeRegister(newGame);
		return lifecycleBean.registerGame(newGame);
	}

	@Override
	public Long updateGame(GameDTO gameDTO) throws PMException {
		Game update = gameDTO.convertToEntity(em);
		lockEntity(Game.class, gameDTO.getID());
		update = validationBean.validateGameBeforeUpdating(update);
		Game updated = lifecycleBean.updateGame(update);
		
		GameDTO updatedGameDTO = createFullGameDTO(updated);
		String json = gson.toJson(updatedGameDTO);
		
		notifier.notifyWithJMS(new Notification(
				NotificationType.GAME_PARAMETERS_CHANGED,
				getAccountIds(update), 
				json));
		
		return update.getID();
	}

	@Override
	public void playerDisconnected(UserJoinDTO userDTO) throws PMException {
		Game game = lockEntity(Game.class, userDTO.getGameId());
		userDTO = validationBean.validateUserJoinInfoBeforeDisconnecting(userDTO);
		lifecycleBean.playerDisconnected(userDTO);
		notifier.notifyWithJMS(new Notification(
				NotificationType.PLAYER_DISCONNECTED, 
				getAccountIds(game),
				gson.toJson(userDTO)));
	}

	@Override
	public void confirmParticipation(UserJoinDTO userDTO) throws PMException {
		Game game = lockEntity(Game.class, userDTO.getGameId());
		userDTO = validationBean.validateUserJoinInfoBeforeAdding(userDTO);
		lifecycleBean.confirmParticipation(userDTO);
		notifier.notifyWithJMS(new Notification(
				NotificationType.PLAYER_CONFIRMED_STAKE, 
				getAccountIds(game),
				gson.toJson(userDTO)));
	}

	@Override
	public void cancelGame(GameDTO gameDTO) throws PMException {
		Game game = lockEntity(Game.class, gameDTO.getID());
		game = validationBean.validateGameBeforeCancelling(game);
		lifecycleBean.cancelGame(game);
		
		notifier.notifyWithJMS(new Notification(
				NotificationType.GAME_CANCELLED, 
				getAccountIds(game),
				"game_id", game.getID()));
	}
	
	@Override
	public void cancelParticipation(UserJoinDTO userDTO) throws PMException {
		Game game = lockEntity(Game.class, userDTO.getGameId());
		userDTO = validationBean.validateUserJoinInfoBeforeDisconnecting(userDTO);
		lifecycleBean.cancelParticipation(userDTO);

		notifier.notifyWithJMS(new Notification(
				NotificationType.PLAYER_CANCELLED_STAKE, 
				getAccountIds(game),
				gson.toJson(userDTO)));
	}

	@Override
	public void startGame(GameDTO gameDTO) throws PMException {
		Game game = gameDTO.convertToEntity(em);
		game = lockEntity(Game.class, gameDTO.getID());
		game = validationBean.validateGameBeforeStart(game);
		lifecycleBean.startGame(game);
		notifier.notifyWithJMS(new Notification(
				NotificationType.GAME_STARTED,
				getAccountIds(game)));
	}

	@Override
	public void resolveResult(ResultInfo resultInfo) throws PMException {
		lockEntity(Game.class, resultInfo.getGameID());
		resultInfo = validationBean.validateResult(resultInfo);
		lifecycleBean.resolveResultAndCloseSession(resultInfo);
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

	private GameDTO createFullGameDTO(Game game) {
		GameDTO dto = new GameDTO();

		dto.setID(game.getID());
		dto.setStake(game.getStake());
		dto.setStatus(game.getStatus().ordinal());
		dto.setType("LOL");

		List<PlayerEntryDTO> playerDTOs = new ArrayList<PlayerEntryDTO>();
		for(PlayerEntry pe : game.getPlayers() ){
			playerDTOs.add(createPlayerDTO(pe));
		}
		
		dto.setPlayerEntries(playerDTOs);
		return dto;
	}

	private PlayerEntryDTO createPlayerDTO(PlayerEntry pe) {
		PlayerEntryDTO dto = new PlayerEntryDTO();
		UserAccount acc = pe.getAccount();
		dto.setAccountId(acc.getID());
		dto.setNick(acc.getNickName());
		dto.setLolAccountId(acc.getLoLAccount().getAccountID());
		dto.setSummonersNick(acc.getLoLAccount().getSummonerInternalName());
		dto.setTeam(pe.getTeam().ordinal());
		
		return dto;
	}

}
