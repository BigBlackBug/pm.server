package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.PlayerValidation;

//json obj
public class PlayerValidationInfo extends AbstractInfo<PlayerValidation> {

	private static final long serialVersionUID = -8702727744206697748L;

	public Long validatorId = 0L;

	public Map<String, String> parameters = new HashMap<String, String>();

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

	@Override
	public PlayerValidation convertToEntity() {
		return null;
	}

}
