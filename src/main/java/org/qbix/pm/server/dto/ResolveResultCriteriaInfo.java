package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.ResolveResultCriteria;

//json obj
public class ResolveResultCriteriaInfo extends AbstractInfo<ResolveResultCriteria> {

	private static final long serialVersionUID = 2549283300751914989L;

	/** resolver id */
	public Long resolver = 0L;
	
	public Map<String, String> params = new HashMap<String, String>();
	
	public void setResolverId(Long resolverId) {
		this.resolver = resolverId;
	}
	
	public Long getResolverId() {
		return resolver;
	}

	public void setParameters(Map<String, String> parameters) {
		this.params = parameters;
	}

	public Map<String, String> getParameters() {
		return params;
	}
	
	@Override
	public ResolveResultCriteria convertToEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}
