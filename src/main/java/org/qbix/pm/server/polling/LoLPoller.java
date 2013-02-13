package org.qbix.pm.server.polling;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoLPoller extends AbstractPoller<PollingResult, PollingParams> {
	private static Logger log = LoggerFactory.getLogger(LoLPoller.class);
	public static final long POLLER_ID = 1L;
	
	private static final String API_KEY = "aNtGZQGcZDfRk3dF63DR";
	private static final String DEFAULT_REGION = "eune";
	private static final String RECENT_GAMES_REQUEST = 
			"http://api.elophant.com/v2/%s/recent_games/%s?key=%s";

	@Override
	protected JsonObject getDataJson(PollingParams params)
			throws PMPollingException {
		Session session = params.getSession();
		Set<PlayerEntry> players = session.getPlayers();

		JsonObject json = assembleJson(players);
		return json;
	}
	
	@Override
	protected JsonObject getDateJson(PollingParams params)
			throws PMPollingException {
		JsonParser parser = new JsonParser();
		Session session = params.getSession();
		PlayerEntry player = session.getPlayers().iterator().next();
		long accountId = player.getAccount().getGamesAccounts()
				.get(UserAccount.GameAccountType.LOL);
		String json = sendRequest(accountId);
		JsonObject parse = parser.parse(json).getAsJsonObject();
		return parse;
	}

	private JsonObject assembleJson(Set<PlayerEntry> players)
			throws PMPollingException {
		JsonObject object = new JsonObject();
		JsonArray array = new JsonArray();
		JsonParser parser = new JsonParser();
		for (PlayerEntry player : players) {
			// long accountId = 32766L;
			long accountId = player.getAccount().getGamesAccounts()
					.get(UserAccount.GameAccountType.LOL);
			String json = sendRequest(accountId);
			JsonElement parse = parser.parse(json);
			array.add(parse);
		}
		object.add("players", array);
		return object;
	}
	
	private String sendRequest(long accountId) throws PMPollingException {
		String url = String.format(RECENT_GAMES_REQUEST, DEFAULT_REGION,
				accountId, API_KEY);
		String json = sendRequest(url);
		if (isDataOk(json)) {
			return json;
		} else {
			throw new PMPollingException("the request has failed",
					ReturnCode.INVALID_DATA_RETURNED);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean isDataOk(String json) {
		Map<String, Object> data = new Gson().fromJson(json, Map.class);
		boolean success = Boolean.parseBoolean((String) data.get("success"));
		List<Map<String, Object>> stats = (List<Map<String, Object>>) data
				.get("gameStatistics");
		return !stats.isEmpty() && success;
	}

}
