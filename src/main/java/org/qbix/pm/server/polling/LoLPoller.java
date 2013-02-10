package org.qbix.pm.server.polling;

import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class LoLPoller extends AbstractPoller<PollingResult, PollingParams> {
	private static final String API_KEY = "aNtGZQGcZDfRk3dF63DR";
	private static Logger log = LoggerFactory.getLogger(HoNPoller.class);

	@Override
	protected JsonObject getJson(PollingParams params) throws PMPollingException{
		Session session = params.getSession();
		List<PlayerEntry> players = session.getPlayers();

		JsonObject json = assembleJson(players);
		return json;
 	}
	
	private JsonObject assembleJson(List<PlayerEntry> players) throws PMPollingException{
		JsonObject object = new JsonObject();
		JsonArray array = new JsonArray();
		JsonParser parser = new JsonParser();
		for (PlayerEntry player : players) {
			long accountId = 32766L;
//			long accountId = player.getAccount().getLolAccountInfo().getAccountId();
			String json = sendRequest(accountId);
			JsonElement parse = parser.parse(json);
			array.add(parse);
		}
		object.add("players", array);
		return object;
	}
	
	private String sendRequest(long accountId) throws PMPollingException{
		ClientRequest request = new ClientRequest(String.format(
				"http://api.elophant.com/v2/na/recent_games/%s?key=%s", accountId, API_KEY));

		request.accept(MediaType.APPLICATION_JSON);
		
		ClientResponse<String> response = null;
		try {
			response = request.get(String.class);
		} catch (Exception e) {
			log.warn("unable to execute request");
			throw new PMPollingException("unable to execute request",
					ReturnCode.UNKNOWN_ERROR);
		}
	
		int statusCode = response.getStatus();
		if (statusCode != 200) {
			log.warn("request failed: error_code = "+ statusCode);
			throw new PMPollingException("unable to execute request", statusCode);
		}
		
		String json = response.getEntity();
		if(isDataOk(json)){
			return json;
		}else{
			throw new PMPollingException("the request has failed", ReturnCode.INVALID_DATA_RETURNED);
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean isDataOk(String json){
		Map<String,Object> data = new Gson().fromJson(json, Map.class);
		boolean success =  Boolean.parseBoolean((String) data.get("success"));
		List<Map<String,Object>> stats = (List<Map<String, Object>>) data.get("gameStatistics");
		return !stats.isEmpty() && success;
	}


}
