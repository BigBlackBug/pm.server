package org.qbix.pm.server.polling;

import org.qbix.pm.server.model.Team;

public class DefaultPollingResult extends PollingResult{

	private Team winnerTeam;

	public Team getWinnerTeam() {
		return winnerTeam;
	}

	public void setWinnerTeam(Team winnerTeam) {
		this.winnerTeam = winnerTeam;
	}
	
}

