package org.qbix.pm.server.model.parser;

import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.VictoryCriteria;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class AbstractParser {
	public SessionTeam getWinner(String json, VictoryCriteria criteria) {
		JsonParser parser = new JsonParser();
		return getWinner((JsonObject) parser.parse(json), criteria);
	}

	protected abstract SessionTeam getWinner(JsonObject parse,
			VictoryCriteria criteria);
}
