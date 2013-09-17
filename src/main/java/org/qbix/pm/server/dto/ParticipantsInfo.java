package org.qbix.pm.server.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionType;

public class ParticipantsInfo implements Serializable {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long sessionId;

	private List<Long> teamOne;

	private List<Long> teamTwo;

	public ParticipantsInfo() {
	}

	public Long getSessionId() {
		return sessionId;
	}

	public List<Long> getTeamOne() {
		return teamOne;
	}

	public List<Long> getTeamTwo() {
		return teamTwo;
	}

	public void setTeamOne(List<Long> teamOne) {
		this.teamOne = teamOne;
	}

	public void setTeamTwo(List<Long> teamTwo) {
		this.teamTwo = teamTwo;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

}
