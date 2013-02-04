package org.qbix.pm.server.model.parser;

import java.util.Map;

import org.qbix.pm.server.model.Team;
import org.qbix.pm.server.model.VictoryCriteria;

public abstract class AbstractParser {
	public abstract Team getWinner(Map<String, String> source, VictoryCriteria criteria);
}
