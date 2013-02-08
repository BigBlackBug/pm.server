package org.qbix.pm.server.polling;

import java.util.Date;

import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.VictoryCriteria;
import org.qbix.pm.server.model.parser.AbstractParser;
import org.qbix.pm.server.util.Cache;

public class HoNPoller extends AbstractPoller<PollingResult, PollingParams> {
	public static transient final long POLLER_ID = 1L;

	@Override
	public PollingResult poll(PollingParams params) throws PMPollingException {
		VictoryCriteria victoryCriteria = params.getSession()
				.getVictoryCriteria();
		Long parserId = victoryCriteria.getParserId();
		AbstractParser parser = Cache.getVCParser(parserId);

		PollingResult ans = new PollingResult();
		ans.setTimestamp(new Date());
		ans.setSession(params.getSession());

		String json = getJson();
		ans.setJsonParams(json);

		ans.setReturnCode(ReturnCode.SUCCESS);// here

		if (json.isEmpty()) {
			ans.setGameFinished(false);
		} else {
			ans.setGameFinished(true);
			SessionTeam winner = parser.getWinner(json, victoryCriteria);
			ans.setWinnerTeam(winner);
		}
		return ans;
	}

	private String getJson() {// FIXME send request
		return "";
	}

}
