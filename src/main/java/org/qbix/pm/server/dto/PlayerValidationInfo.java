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
	public PlayerValidation convertToEntity() {
		return null;
	}

}
