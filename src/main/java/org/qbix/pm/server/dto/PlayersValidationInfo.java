package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.PlayersValidation;

//json obj
public class PlayersValidationInfo extends AbstractInfo<PlayersValidation> {

	private static final long serialVersionUID = -8702727744206697748L;

	/** validator id */
	public Long validator = 0L;

	public Map<String, String> params = new HashMap<String, String>();

	public void setValidator(Long validator) {
		this.validator = validator;
	}
	
	public Long getValidator() {
		return validator;
	}
	
	public void setParams(Map<String, String> params) {
		this.params = params;
	}
	
	public Map<String, String> getParams() {
		return params;
	}

	@Override
	public PlayersValidation convertToEntity() {
		PlayersValidation validation = new PlayersValidation();
		validation.setValidatorId(validator);
		validation.setParameters(params);
		return validation;
	}

}
