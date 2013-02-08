package org.qbix.pm.server.model.parser;

import java.util.Map;
import java.util.Objects;

import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.VictoryCriteria;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class HoNParser extends AbstractParser{

	public static final Long PARSER_ID = 10L;

	@Override
	protected SessionTeam getWinner(JsonObject parse, VictoryCriteria criteria) {
		return SessionTeam.TEAM_0;
	}
}
