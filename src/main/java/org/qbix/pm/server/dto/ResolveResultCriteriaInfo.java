package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.ResolveResultCriteria;

//json obj
public class ResolveResultCriteriaInfo extends AbstractInfo<ResolveResultCriteria> {

	private static final long serialVersionUID = 2549283300751914989L;

	public Long resolverId = 0L;
	
	public Map<String, String> parameters = new HashMap<String, String>();
	
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
	
	@Override
	public ResolveResultCriteria convertToEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}
