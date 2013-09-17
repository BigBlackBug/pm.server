package org.qbix.pm.server.dto;

import java.io.Serializable;
import java.util.Map;

public class ParticipantsReturnInfo implements Serializable {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long sessionId;

	private Map<Long, Entry> teamOne;

	private Map<Long, Entry> teamTwo;

	public ParticipantsReturnInfo() {
	}

	public static class Entry {
		private final Long pmAccountID;
		private final String pmNickName;

		public Entry(Long pmAccountID, String pmNickName) {
			this.pmAccountID = pmAccountID;
			this.pmNickName = pmNickName;
		}

		public Long getPmAccountID() {
			return pmAccountID;
		}

		public String getPmNickName() {
			return pmNickName;
		}

	}

	public void putToTeamOne(Long lolAccountID, Entry entry) {
		teamOne.put(lolAccountID, entry);
	}

	public void putToTeamTwo(Long lolAccountID, Entry entry) {
		teamOne.put(lolAccountID, entry);
	}

	public Map<Long, Entry> getTeamOne() {
		return teamOne;
	}

	public void setTeamOne(Map<Long, Entry> teamOne) {
		this.teamOne = teamOne;
	}

	public Map<Long, Entry> getTeamTwo() {
		return teamTwo;
	}

	public void setTeamTwo(Map<Long, Entry> teamTwo) {
		this.teamTwo = teamTwo;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

}
