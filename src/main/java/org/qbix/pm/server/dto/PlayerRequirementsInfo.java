package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.PlayerRequirements;

//json obj
//player_requirements : {
//	"parser_id" : 2,
//	"requirements" : { 
//		"rating" : {"$gt" : 1700},
//		"kdr" : {"$gt" : 1.56}
//	}
//}
public class PlayerRequirementsInfo extends AbstractInfo<PlayerRequirements> {

	private static final long serialVersionUID = -8702727744206697748L;

	/** parser id */
	public Long parserId = 0L;

	public Map<String, String> requirements = new HashMap<String, String>();

	public void setParserId(Long parserId) {
		this.parserId = parserId;
	}
	
	public Long getParserId() {
		return parserId;
	}
	
	public void setRequirements(Map<String, String> requirements) {
		this.requirements = requirements;
	}
	
	public Map<String, String> getRequriements() {
		return requirements;
	}

	@Override
	public PlayerRequirements convertToEntity() {
		PlayerRequirements validation = new PlayerRequirements();
		validation.setValidatorId(parserId);
		validation.setParameters(requirements);
		return validation;
	}

}
