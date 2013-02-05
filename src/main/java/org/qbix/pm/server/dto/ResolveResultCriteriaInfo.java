package org.qbix.pm.server.dto;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.ResolveResultCriteria;

//json
public class ResolveResultCriteriaInfo extends
		AbstractInfo<ResolveResultCriteria> {

	private static final long serialVersionUID = 2549283300751914989L;

	/** resolver id */
	public Long resolver = 0L;

	public Map<String, Object> params = new HashMap<String, Object>();

	public void setResolver(Long resolver) {
		this.resolver = resolver;
	}

	public Long getResolver() {
		return resolver;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	@Override
	public ResolveResultCriteria convertToEntity() {
		ResolveResultCriteria criteria = new ResolveResultCriteria();
		criteria.setResolverId(resolver);
		criteria.setParameters(serializeMapParams(params));
		return criteria;
	}

}
