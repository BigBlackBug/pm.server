package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.PlayersValidation;

import com.google.gson.Gson;


//json obj
public class PlayersValidationInfo extends AbstractInfo<PlayersValidation> {

	private static final long serialVersionUID = -8702727744206697748L;

	/** validator id */
	public Long validator = 0L;

	public Map<String, Object> params = new HashMap<String, Object>();

	public void setValidator(Long validator) {
		this.validator = validator;
	}

	public Long getValidator() {
		return validator;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	@Override
	public PlayersValidation convertToEntity() {
		PlayersValidation validation = new PlayersValidation();
		validation.setValidatorId(validator);
		validation.setJsonParams(new Gson().toJson(params));
		return validation;
	}

}
