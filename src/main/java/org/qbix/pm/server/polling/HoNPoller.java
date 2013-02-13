package org.qbix.pm.server.polling;

import java.util.List;
import java.util.Set;

import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.UserAccount;
import org.qbix.pm.server.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class HoNPoller extends AbstractPoller<PollingResult, PollingParams> {
	public static transient final long POLLER_ID = 0L;
	private static final String API_TOKEN = "YH34V9ZLJ42568WQ";
	private static final String MATCH_HISTORY_REQUEST = 
			"http://api.heroesofnewerth.com/match_history/public/accountid/%s/?token=%s";
	private static final String MATCH_STATS_REQUEST = 
			"http://api.heroesofnewerth.com/match/summ/matchid/%s/?token=%s";
	private static final String MATCH_ALL_STATS_REQUEST = 
			"http://api.heroesofnewerth.com/match/statistics/matchid/%s/?token=%s";
	
	private static Logger log = LoggerFactory.getLogger(HoNPoller.class);

	@Override
	protected JsonObject getDataJson(PollingParams params)
			throws PMPollingException {
		JsonObject matchInfo = getDateJson(params);
		long matchId = matchInfo.get("match_id").getAsLong();
		String statsUrl = String.format(MATCH_ALL_STATS_REQUEST, matchId, API_TOKEN);
		String matchStatsJson = sendRequest(statsUrl);
		JsonParser parser = new JsonParser();
		JsonObject result = new JsonObject();
		result.add("short_stats", matchInfo);
		result.add("per_player_stats", parser.parse(matchStatsJson));
		return result;
	}

	@Override
	protected JsonObject getDateJson(PollingParams params)
			throws PMPollingException {
		JsonParser parser = new JsonParser();
		Set<PlayerEntry> players = params.getSession().getPlayers();
		PlayerEntry player = players.iterator().next();
		long accountId = player.getAccount().getGamesAccounts()
				.get(UserAccount.GameAccountType.HON);
		
		String url = String.format(MATCH_HISTORY_REQUEST, accountId, API_TOKEN);
		String matchHistoryJson = sendRequest(url);
		JsonObject matchHistory = parser.parse(matchHistoryJson).getAsJsonObject();
		String historyString = matchHistory.get("history").getAsString();
		List<String> pieces = StringUtils.splitAndOmitAllEmpty(historyString, ",");
		String lastGameToken = pieces.get(0);
		String lastGameId = StringUtils.splitAndOmitAllEmpty(lastGameToken, "|").get(0);
		String statsUrl = String.format(MATCH_STATS_REQUEST, lastGameId, API_TOKEN);
		
		String matchStatsJson = sendRequest(statsUrl);
		return parser.parse(matchStatsJson).getAsJsonObject();
	}
}
