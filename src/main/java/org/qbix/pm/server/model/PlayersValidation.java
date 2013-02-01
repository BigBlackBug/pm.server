package org.qbix.pm.server.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class PlayersValidation extends AbstractEntity {
	
	private static final long serialVersionUID = -4732397574586841800L;
	
	public Long validatorId = 0L;
	
	@ElementCollection
	public Map<String, String> parameters = new HashMap<String, String>();

	public PlayersValidation() {
	}
	
	public void setValidatorId(Long validatorId) {
		this.validatorId = validatorId;
	}
	
	public Long getValidatorId() {
		return validatorId;
	}

	public void setParameters(Map<String, String> parameters) {
		this.parameters = parameters;
	}

	public Map<String, String> getParameters() {
		return parameters;
	}
}
