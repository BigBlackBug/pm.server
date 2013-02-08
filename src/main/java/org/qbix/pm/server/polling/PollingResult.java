package org.qbix.pm.server.polling;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.qbix.pm.server.model.EntityWithSerializedParams;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionTeam;

@Entity
public class PollingResult extends EntityWithSerializedParams {

	private static final long serialVersionUID = -633501241327042166L;
	
	protected boolean gameFinished = false;
	
	private Session session;

	@Transient
	private ReturnCode returnCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	private SessionTeam winnerTeam;

	public SessionTeam getWinnerTeam() {
		return winnerTeam;
	}

	public void setWinnerTeam(SessionTeam winnerTeam) {
		this.winnerTeam = winnerTeam;
	}
	public Session getSession() {
		return session;
	}

	public ReturnCode getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(ReturnCode returnCode) {
		this.returnCode = returnCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void setGameFinished(boolean gameFinished) {
		this.gameFinished = gameFinished;
	}

	public boolean isGameFinished() {
		return gameFinished;
	}

}
