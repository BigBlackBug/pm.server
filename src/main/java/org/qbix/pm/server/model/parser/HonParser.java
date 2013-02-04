package org.qbix.pm.server.model.parser;

import java.util.Map;

import org.qbix.pm.server.model.Team;
import org.qbix.pm.server.model.VictoryCriteria;

import com.google.gson.Gson;

public class HonParser extends AbstractParser{
	
	@Override
	public Team getWinner(Map<String, String> source, VictoryCriteria criteria) {
		
		return Team.TEAM_0;
	}

}
