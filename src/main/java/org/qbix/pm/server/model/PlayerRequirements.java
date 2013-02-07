package org.qbix.pm.server.model;

import javax.persistence.Entity;

@Entity
public class PlayerRequirements extends EntityWithSerializedParams {

	private static final long serialVersionUID = -4732397574586841800L;

	public Long validatorId = 0L;

	public PlayerRequirements() {
	}

	public void setValidatorId(Long validatorId) {
		this.validatorId = validatorId;
	}

	public Long getValidatorId() {
		return validatorId;
	}
}
