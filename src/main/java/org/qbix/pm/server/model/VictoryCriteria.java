package org.qbix.pm.server.model;

import javax.persistence.Entity;

@Entity
public class VictoryCriteria extends EntityWithSerializedParams {

	private static final long serialVersionUID = -2057207223808564357L;

	public Long parserId = 0L;

	public VictoryCriteria() {
	}

	public Long getParserId() {
		return parserId;
	}
	
	public void setParserId(Long parserId) {
		this.parserId = parserId;
	}

}
