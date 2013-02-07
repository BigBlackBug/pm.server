package org.qbix.pm.server.polling;

import javax.persistence.Entity;

import org.qbix.pm.server.model.AbstractEntity;

@Entity
public class PollingResult extends AbstractEntity {

	private static final long serialVersionUID = -633501241327042166L;
	
	protected boolean gameFinished = false;

	public void setGameFinished(boolean gameFinished) {
		this.gameFinished = gameFinished;
	}

	public boolean isGameFinished() {
		return gameFinished;
	}

}
