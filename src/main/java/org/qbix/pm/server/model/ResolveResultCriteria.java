package org.qbix.pm.server.model;

import javax.persistence.Entity;

@Entity
public class ResolveResultCriteria extends EntityWithSerializedParams {

	private static final long serialVersionUID = -2057207223808564357L;

	public Long resolverId = 0L;

	public ResolveResultCriteria() {
	}

	public void setResolverId(Long resolverId) {
		this.resolverId = resolverId;
	}

	public Long getResolverId() {
		return resolverId;
	}

}
