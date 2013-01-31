package org.qbix.pm.server.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class ResolveResultCriteria extends AbstractEntity {

	private static final long serialVersionUID = -2057207223808564357L;

	public Long resolverId = 0L;
	
	@ElementCollection
	public Map<String, String> parameters = new HashMap<String, String>();

	public ResolveResultCriteria() {
	}
	
	public void setResolverId(Long resolverId) {
		this.resolverId = resolverId;
	}
	
	public Long getResolverId() {
		return resolverId;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}

}
