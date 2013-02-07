package org.qbix.pm.server.model.parser;

import java.util.Map;

import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.VictoryCriteria;

public abstract class AbstractParser {
	public abstract SessionTeam getWinner(Map<String, String> source, VictoryCriteria criteria);
}
