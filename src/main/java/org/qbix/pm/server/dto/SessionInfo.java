package org.qbix.pm.server.dto;

import org.qbix.pm.server.model.Session;

//json obj
public class SessionInfo extends AbstractInfo<Session> {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long sessionId;

	private PlayerValidationInfo playerValidation;

	private ResolveResultCriteriaInfo resolveResultCriteria;

	public SessionInfo() {
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setPlayerValidation(PlayerValidationInfo playerValidation) {
		this.playerValidation = playerValidation;
	}

	public PlayerValidationInfo getPlayerValidation() {
		return playerValidation;
	}

	public void setResolveResultCriteria(
			ResolveResultCriteriaInfo resolveResultCriteria) {
		this.resolveResultCriteria = resolveResultCriteria;
	}

	public ResolveResultCriteriaInfo getResolveResultCriteria() {
		return resolveResultCriteria;
	}

	@Override
	public String toString() {
		return String.format("SESSION id=%d, pv=%s, rrc=%s", sessionId,
				playerValidation, resolveResultCriteria);
	}

	@Override
	public Session convertToEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}
