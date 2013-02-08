package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.PlayerRequirements;

import com.google.gson.Gson;

//json obj
public class PlayerRequirementsInfo extends AbstractInfo<PlayerRequirements> {

	private static final long serialVersionUID = -8702727744206697748L;

	/** parser id */
	public Long parserId = 0L;

	public Map<String, Object> requirements = new HashMap<String, Object>();

	public void setParserId(Long parserId) {
		this.parserId = parserId;
	}
	
	public Long getParserId() {
		return parserId;
	}
	
	public Map<String, Object> getRequirements() {
		return requirements;
	}
	
	public void setRequirements(Map<String, Object> requirements) {
		this.requirements = requirements;
	}

	@Override
	public PlayerRequirements convertToEntity() {
		PlayerRequirements validation = new PlayerRequirements();
		validation.setValidatorId(parserId);
		validation.setJsonParams(new Gson().toJson(requirements));
		return validation;
	}

}
