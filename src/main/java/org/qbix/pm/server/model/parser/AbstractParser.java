package org.qbix.pm.server.model.parser;

import java.util.Date;

import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionTeam;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class AbstractParser {
	public SessionTeam getWinner(String json, Session session) {
		JsonParser parser = new JsonParser();
		return getWinner((JsonObject) parser.parse(json), session);
	}
	
	public boolean isGameFinished(String json, Session session){
		JsonParser parser = new JsonParser();
		return isGameFinished((JsonObject) parser.parse(json), session.getSessionStartDate());
	}
	
	public boolean isGameFinished(JsonObject json, Session session){
		return isGameFinished(json, session.getSessionStartDate());
	}

	protected abstract boolean isGameFinished(JsonObject json, Date sessionStartDate);

	public abstract SessionTeam getWinner(JsonObject json, Session session);
	
	protected Date parseDate(String jsonDate) {
		// "/Date(1321867151710)/"
		int idx1 = jsonDate.indexOf("(");
		int idx2 = jsonDate.indexOf(")");
		String value = jsonDate.substring(idx1 + 1, idx2);
		return new Date(Long.valueOf(value));
	}
}
