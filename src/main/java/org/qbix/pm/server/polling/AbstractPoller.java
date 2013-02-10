package org.qbix.pm.server.polling;

import java.util.Date;

import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.VictoryCriteria;
import org.qbix.pm.server.model.parser.AbstractParser;
import org.qbix.pm.server.util.ServiceUnitHolder;

import com.google.gson.JsonObject;

public abstract class AbstractPoller<T extends PollingResult, P extends PollingParams> {

	@SuppressWarnings("unchecked")
	public T poll(P params) throws PMPollingException {
		Session session = params.getSession();
		VictoryCriteria victoryCriteria = session.getVictoryCriteria();
		Long parserId = victoryCriteria.getParserId();
		AbstractParser parser = ServiceUnitHolder.getVCParser(parserId);

		PollingResult ans = new PollingResult();
		ans.setTimestamp(new Date());
		ans.setSession(params.getSession());
		JsonObject json = null;
		try {
			json = getJson(params);
		} catch (PMPollingException ex) {
			ans.setReturnCode(ReturnCode.valueOf(ex.getCode()));//TODO may be different
			return (T)ans;
		}

		ans.setJsonParams(json.toString());
		ans.setReturnCode(ReturnCode.SUCCESS);// here

		boolean gameFinished = parser.isGameFinished(json, session);
		ans.setGameFinished(gameFinished);
		if (gameFinished) {
			SessionTeam winner = parser.getWinner(json, session);
			ans.setWinnerTeam(winner);
		}
		return (T) ans;
	}

	protected abstract JsonObject getJson(P params) throws PMPollingException;
	
}
