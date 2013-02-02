package org.qbix.pm.server.polling;

public abstract class PollingResult {

	protected boolean gameFinished = false;
	
	public void setGameFinished(boolean gameFinished) {
		this.gameFinished = gameFinished;
	}
	
	public boolean isGameFinished() {
		return gameFinished;
	}
	
}
