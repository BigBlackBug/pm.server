package org.qbix.pm.server.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

@Entity
public class Session extends AbstractEntity {

	private static final long serialVersionUID = 414967553274661010L;

	@Enumerated(EnumType.STRING)
	private SessionStatus status = SessionStatus.NOT_EXIST;

	@OneToOne(cascade = CascadeType.ALL)
	private PlayerValidation playerValidation;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private ResolveResultCriteria resolveResultCriteria;

	public Session() {
	}

	public void setPlayerValidation(PlayerValidation playerValidation) {
		this.playerValidation = playerValidation;
	}

	public PlayerValidation getPlayerValidation() {
		return playerValidation;
	}

	public void setResolveResultCriteria(
			ResolveResultCriteria resolveResultCriteria) {
		this.resolveResultCriteria = resolveResultCriteria;
	}

	public ResolveResultCriteria getResolveResultCriteria() {
		return resolveResultCriteria;
	}

	public void setStatus(SessionStatus status) {
		this.status = status;
	}

	public SessionStatus getStatus() {
		return status;
	}

}
