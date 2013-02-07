package org.qbix.pm.server.polling;

import org.qbix.pm.server.model.SessionTeam;

public class DefaultPollingResult extends PollingResult{

	private SessionTeam winnerTeam;

	public SessionTeam getWinnerTeam() {
		return winnerTeam;
	}

	public void setWinnerTeam(SessionTeam winnerTeam) {
		this.winnerTeam = winnerTeam;
	}
	
}

