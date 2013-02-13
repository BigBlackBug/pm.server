package org.qbix.pm.server.model.parser;

import java.util.Date;

import org.qbix.pm.server.exceptions.PMParsingException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionTeam;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public abstract class AbstractParser {
	public SessionTeam getWinner(String json, Session session) throws PMParsingException {
		JsonObject jsonObject = parseJson(json);
		return getWinner(jsonObject, session);
	}

	private JsonObject parseJson(String json) throws PMParsingException{
		JsonParser parser = new JsonParser();
		try{
			return parser.parse(json).getAsJsonObject();
		}catch(JsonParseException ex){
			throw new PMParsingException(ex.getMessage());
		}
	}
	
	public boolean isGameFinished(String json, Session session)
			throws PMParsingException {
		JsonObject jsonObject = parseJson(json);
		return isGameFinished(jsonObject,
				session.getSessionStartDate());
	}

	public boolean isGameFinished(JsonObject json, Session session)
			throws PMParsingException {
		return isGameFinished(json, session.getSessionStartDate());
	}

	protected abstract boolean isGameFinished(JsonObject json,
			Date sessionStartDate) throws PMParsingException;

	public abstract SessionTeam getWinner(JsonObject json, Session session)
			throws PMParsingException;

	protected Date parseDate(String jsonDate) {
		// "/Date(1321867151710)/"
		int idx1 = jsonDate.indexOf("(");
		int idx2 = jsonDate.indexOf(")");
		String value = jsonDate.substring(idx1 + 1, idx2);
		return new Date(Long.valueOf(value));
	}
}
