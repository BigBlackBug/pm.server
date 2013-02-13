package org.qbix.pm.server.model.parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.qbix.pm.server.exceptions.PMParsingException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.VictoryCriteria;
import org.qbix.pm.server.util.modifiers.IntModifiers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class HoNParser extends AbstractParser{
	private static Logger log = LoggerFactory.getLogger(HoNParser.class);
	
	public static final Long PARSER_ID = 10L;
	private static final DateFormat HON_INPUT_DATE_FORMATTER  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static enum HoNTeam{
		LEGION(1, SessionTeam.TEAM_0),
		HELLBOURNE(2, SessionTeam.TEAM_1);
		
		public int honCode;
		public SessionTeam team;
		
		private HoNTeam(int honCode, SessionTeam team) {
			this.honCode = honCode;
			this.team = team;
		}
		
		public static HoNTeam valueOf(int honCode) throws IllegalArgumentException{
			if(honCode == LEGION.honCode){
				return HoNTeam.LEGION;
			}else{
				return HoNTeam.HELLBOURNE;
			}
		}
		public static HoNTeam otherValue(int honCode) throws IllegalArgumentException{
			if(honCode == LEGION.honCode){
				return HoNTeam.HELLBOURNE;
			}else{
				return HoNTeam.LEGION;
			}
		}
	}
	
	@Override
	protected boolean isGameFinished(JsonObject json, Date sessionStartDate)
			throws PMParsingException {
		String date = json.get("mdt").getAsString();
		Date parse = null;
		try {
			parse = HON_INPUT_DATE_FORMATTER.parse(date);
		} catch (ParseException e) {
			throw new PMParsingException("invalid response from server", e);
		}
		return parse.after(sessionStartDate);
	}
	
	@Override
	public SessionTeam getWinner(JsonObject json, Session session)
			throws PMParsingException {
		JsonObject shortStats = json.get("short_stats").getAsJsonObject();
		JsonArray playerArray = json.get("per_player_stats").getAsJsonArray();
		// TODO maybe cut out all the irrelevant stuff out? like other games
		Map<String, Object> criteria = session.getVictoryCriteria()
				.getParamsMap();
		Map<String, Object> team = (Map<String, Object>) criteria.get("team");
		Integer id = (Integer) team.get("id");
		SessionTeam criteriaTeam = SessionTeam.valueOf(id);
		Integer winner = (Integer) team.get("winner");
		HoNTeam criteriaWinnerTeam = HoNTeam.valueOf(winner);

		boolean teamCriteriaSatisfied;
		boolean killCriteriaSatisfied;
		boolean timeCriteriaSatisfied;
		
		if (!isRightGame(playerArray, session.getPlayers())) {
			// TODO GET TO THE CHOPPER WE'RE FUCKED
		}

		HoNTeam realWinner = parseWinner(playerArray.get(0).getAsJsonObject());

		teamCriteriaSatisfied = (realWinner == criteriaWinnerTeam);
		killCriteriaSatisfied = isKillCriteriaSatisfied(team, playerArray);
		timeCriteriaSatisfied = isTimeCriteriaSatisfied(shortStats,session.getVictoryCriteria());

		if (teamCriteriaSatisfied && killCriteriaSatisfied && timeCriteriaSatisfied) {
			return criteriaTeam;
		} else {
			return HoNTeam.otherValue(realWinner.honCode).team;
		}
	}
//time_criteria:{
//	value: {"$eq" : 15}
//	threhsold : 3
//}
	private boolean isTimeCriteriaSatisfied(JsonObject shortStats,
			VictoryCriteria victoryCriteria) {
		int realTimePlayed = shortStats.get("time_played").getAsInt();
		
		Map<String,Object> timeCriteriaObject= (Map<String, Object>) victoryCriteria.getParamsMap().get("time_criteria");
		Integer threshold;
		threshold = (Integer) timeCriteriaObject.get("threshold");
		if(threshold == null){
			threshold = 0;
		}
		
		Map<String,Object> valueObject = (Map<String, Object>) timeCriteriaObject.get("value");
		String key = valueObject.keySet().iterator().next();
		IntModifiers modifier = IntModifiers.getValueOf(key);
		Integer value = (Integer) valueObject.get(key);
		boolean result = modifier.satisfies(realTimePlayed, value, threshold);
		return result;
	}

//	{
//	"account_id" : "777",
//	"kills" : {"$gt" : 15}
//}
	@SuppressWarnings("unchecked")
	private boolean isKillCriteriaSatisfied(Map<String, Object> team, JsonArray jsonPlayers) {
		boolean result = true;
		List<Map<String, Object>> players = (List<Map<String, Object>>) team.get("players");
		
		for (Map<String, Object> player : players) {
			Integer accId = (Integer) player.get("account_id");
			JsonObject jsonPlayer = getForAccId(jsonPlayers, accId);
			int realKillCount = jsonPlayer.get("herokills").getAsInt();
			
			Map<String,Object> killsObject = (Map<String, Object>) player.get("kills");
			
			String key = killsObject.keySet().iterator().next();
			IntModifiers modifier = IntModifiers.getValueOf(key);
			Integer threshold = (Integer) killsObject.get(key);
			result &= modifier.satisfies(realKillCount, threshold);
		}
		return result;
	}

	private JsonObject getForAccId(JsonArray jsonPlayers, Integer accId) {
		for (JsonElement player : jsonPlayers) {
			JsonObject playerObject = player.getAsJsonObject();
			long accountId = playerObject.get("account_id").getAsLong();
			if(accountId == accId){
				return playerObject;
			}
		}
		return null;//never gonna happen
	}

	private boolean isRightGame(JsonArray playerArray, Set<PlayerEntry> players) {
		return true;
	}

	private HoNTeam parseWinner(JsonObject dataPiece) {
		int teamId = dataPiece.get("team").getAsInt();
		int won = dataPiece.get("wins").getAsInt();
		HoNTeam team = HoNTeam.valueOf(teamId);
		return won == 1 ? team : HoNTeam.otherValue(teamId);
	}
	
}
