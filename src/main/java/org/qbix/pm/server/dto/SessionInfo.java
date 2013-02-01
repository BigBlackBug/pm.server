package org.qbix.pm.server.dto;

import org.qbix.pm.server.model.Session;

//json obj
public class SessionInfo extends AbstractInfo<Session> {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long sessid;

	private PlayerValidationInfo pv;

	private ResolveResultCriteriaInfo rrc;

	public SessionInfo() {
	}

	public void setSessionId(Long sessionId) {
		this.sessid = sessionId;
	}

	public Long getSessionId() {
		return sessid;
	}

	public void setPlayerValidation(PlayerValidationInfo playerValidation) {
		this.pv = playerValidation;
	}

	public PlayerValidationInfo getPlayerValidation() {
		return pv;
	}

	public void setResolveResultCriteria(
			ResolveResultCriteriaInfo resolveResultCriteria) {
		this.rrc = resolveResultCriteria;
	}

	public ResolveResultCriteriaInfo getResolveResultCriteria() {
		return rrc;
	}

	@Override
	public String toString() {
		return String.format("SESSION id=%d, pv=%s, rrc=%s", sessid,
				pv, rrc);
	}

	@Override
	public Session convertToEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}
