package org.qbix.pm.server.model.parser;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.util.modifiers.IntModifiers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class LoLParser extends AbstractParser{

	private static enum LoLTeam{
		TEAM_0(100,SessionTeam.TEAM_0),
		TEAM_1(200,SessionTeam.TEAM_1);
		
		public int lolCode;
		public SessionTeam team;
		
		private LoLTeam(int lolCode, SessionTeam team) {
			this.lolCode = lolCode;
			this.team = team;
		}
		
		public static LoLTeam valueOf(int lolCode) throws IllegalArgumentException{
			if(lolCode == TEAM_0.lolCode){
				return LoLTeam.TEAM_0;
			}else{
				return LoLTeam.TEAM_1;
			}
		}
		
		public static LoLTeam otherValue(int lolCode) throws IllegalArgumentException{
			if(lolCode == TEAM_0.lolCode){
				return LoLTeam.TEAM_1;
			}else{
				return LoLTeam.TEAM_0;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public SessionTeam getWinner(JsonObject json, Session session) {
		//TODO maybe cut out all the irrelevant stuff out? like other games
		Map<String, Object> criteria = session.getVictoryCriteria().getParamsMap();
		Map<String, Object> team = (Map<String, Object>) criteria.get("team");
		Integer id = (Integer) team.get("id");
		SessionTeam criteriaTeam = SessionTeam.valueOf(id);
		Integer winner = (Integer) team.get("winner");
		LoLTeam criteriaWinnerTeam = LoLTeam.valueOf(winner);
				
		
		boolean teamCriteriaSatisfied;
		boolean killCriteriaSatisfied;

		JsonObject lastGame = getLastGame(json);
		JsonArray jsonPlayers = lastGame.get("fellowPlayers").getAsJsonArray();
		
		if(!isRightGame(jsonPlayers, session.getPlayers())){
			//TODO GET TO THE CHOPPER WE'RE FUCKED
		}
		
		LoLTeam realWinner = parseWinner(lastGame);
		
		teamCriteriaSatisfied = (realWinner == criteriaWinnerTeam);
		killCriteriaSatisfied = isKillCriteriaSatisfied(team, json);
		
		if(teamCriteriaSatisfied && killCriteriaSatisfied){
			return criteriaTeam;
		}else{
			return LoLTeam.otherValue(realWinner.lolCode).team;
		}
	}

//	{
//	"account_id" : "777",
//	"kills" : {"$gt" : 15}
//}
	@SuppressWarnings("unchecked")
	private boolean isKillCriteriaSatisfied(Map<String, Object> team, JsonObject json) {
		boolean result = true;
		JsonArray data = getKillDataArray(json);
		List<Map<String, Object>> players = (List<Map<String, Object>>) team.get("players");
		
		for (Map<String, Object> player : players) {
			Integer accId = (Integer) player.get("account_id");
			JsonObject jsonPlayer = getForAccId(data, accId);
			int realKillCount = jsonPlayer.get("kills").getAsInt();
			
			Map<String,Object> killsObject = (Map<String, Object>) player.get("kills");
			
			String key = killsObject.keySet().iterator().next();
			IntModifiers modifier = IntModifiers.getValueOf(key);
			Integer threshold = (Integer) killsObject.get(key);
			result &= modifier.satisfies(realKillCount, threshold);
		}
		return result;
	}
	
	private JsonObject getForAccId(JsonArray players, int accountId){
		int i;
		for (i=0;i<players.size();i++) {
			JsonObject player = players.get(i).getAsJsonObject();
			int playerAccId = player.get("account_id").getAsInt();
			if(playerAccId == accountId){
				return player;
			}
		}
		return null;
	}
	
	//[{account_id:777,kills:10}]

	private JsonArray getKillDataArray(JsonObject json) {
		JsonArray result = new JsonArray();
		JsonArray players = json.get("players").getAsJsonArray();
		for (JsonElement player : players) {
			JsonObject data = player.getAsJsonObject().get("data").getAsJsonObject();
			JsonArray gameStats = data.get("gameStatistics").getAsJsonArray();
			JsonObject lastGame = gameStats.get(0).getAsJsonObject();
			JsonArray stats = lastGame.getAsJsonObject().get("statistics").getAsJsonArray();
			int killCount = 0;
			for (JsonElement stat : stats) {
				JsonObject statObject = stat.getAsJsonObject();
				String statType = statObject.get("statType").getAsString();
				if(statType.equals("CHAMPIONS_KILLED")){
					killCount = statObject.get("value").getAsInt();
					break;
				}
			}
			
			JsonObject entry = new JsonObject();
			int accountId = data.get("userId").getAsInt();
			entry.add("account_id", new JsonPrimitive(accountId));
			entry.add("kills", new JsonPrimitive(killCount));
			result.add(entry);
		}
		return result;
	}

	private JsonObject getLastGame(JsonObject json) {
		JsonArray players = json.get("players").getAsJsonArray();
		JsonElement data = players.get(0).getAsJsonObject().get("data");
		JsonArray games = data.getAsJsonObject().get("gameStatistics").getAsJsonArray();
		JsonObject lastGame = games.get(games.size()-1).getAsJsonObject();
		return lastGame;
	}

	private boolean isRightGame(JsonArray jsonPlayers, Set<PlayerEntry> realsonObPlayers){
		return true;
	}
	
	private LoLTeam parseWinner(JsonObject lastGame){
		int teamId = lastGame.get("teamId").getAsInt();
		LoLTeam playerTeam = LoLTeam.valueOf(teamId);
		LoLTeam otherTeam = LoLTeam.valueOf(teamId);
		
		JsonArray gameStats = lastGame.get("statistics").getAsJsonArray();
		
		for(JsonElement stat:gameStats){
			String statType = stat.getAsJsonObject().get("statType").getAsString();
			if(statType.equals("WIN")){
				return playerTeam;
			} else if (statType.equals("LOSE")){
				return otherTeam;
			}
		}
		return null;//will never happen
	}

	@Override
	protected boolean isGameFinished(JsonObject json, Date sessionStartDate) {
		JsonObject data = json.get("data").getAsJsonObject();
		JsonArray stats = data.get("gameStatistics").getAsJsonArray();
		JsonObject game = stats.get(stats.size()-1).getAsJsonObject();//most recent game
		String creationDate = game.get("createDate").getAsString();
		return parseDate(creationDate).after(sessionStartDate);
	}

}
