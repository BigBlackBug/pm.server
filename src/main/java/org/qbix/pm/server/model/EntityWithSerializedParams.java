package org.qbix.pm.server.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.google.gson.Gson;

@MappedSuperclass
public abstract class EntityWithSerializedParams extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column(length = 4096)
	protected String parameters;

	private transient Map<String, Object> paramsMap;

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getParameters() {
		return parameters;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getParamsMap() {

		if (paramsMap != null) {
			return paramsMap;
		}
		paramsMap = new Gson().fromJson(parameters, Map.class);
		return paramsMap;
	}
}
