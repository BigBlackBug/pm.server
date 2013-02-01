package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.PlayerValidation;

//json obj
public class PlayerValidationInfo extends AbstractInfo<PlayerValidation> {

	private static final long serialVersionUID = -8702727744206697748L;

	/** validator id */
	public Long validator = 0L;

	public Map<String, String> params = new HashMap<String, String>();

	public void setValidatorId(Long validatorId) {
		this.validator = validatorId;
	}

	public Long getValidatorId() {
		return validator;
	}

	public void setParameters(Map<String, String> parameters) {
		this.params = parameters;
	}

	public Map<String, String> getParameters() {
		return params;
	}

	@Override
	public PlayerValidation convertToEntity() {
		return null;
	}

}
