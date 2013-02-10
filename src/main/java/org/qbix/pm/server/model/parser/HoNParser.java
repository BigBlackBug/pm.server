package org.qbix.pm.server.model.parser;

import java.util.Date;

import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionTeam;

import com.google.gson.JsonObject;

public class HoNParser extends AbstractParser{

	public static transient final long PARSER_ID = 100L;

	@Override
	public boolean isGameFinished(JsonObject json, Date sessionStartDate) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SessionTeam getWinner(JsonObject json, Session session) {
		// TODO Auto-generated method stub
		return null;
	}

}
