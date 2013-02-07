package org.qbix.pm.server.model.parser;

import java.util.Map;

import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.VictoryCriteria;

import com.google.gson.Gson;

public class HonParser extends AbstractParser{
	
	@Override
	public SessionTeam getWinner(Map<String, String> source, VictoryCriteria criteria) {
		
		return SessionTeam.TEAM_0;
	}

}
