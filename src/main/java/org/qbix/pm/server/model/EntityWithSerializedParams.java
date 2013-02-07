package org.qbix.pm.server.model;

import java.util.Collections;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.google.gson.Gson;

@MappedSuperclass
public abstract class EntityWithSerializedParams extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Column(length = 4096, name = "json_params")
	protected String jsonParams;

	private transient Map<String, Object> paramsMap;

	public void setJsonParams(String parameters) {
		this.jsonParams = parameters;
	}

	public String getJsonParams() {
		return jsonParams;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getParamsMap() {
		if (paramsMap != null) {
			return paramsMap;
		}

		if (getJsonParams() == null) {
			return Collections.EMPTY_MAP;
		}

		paramsMap = new Gson().fromJson(jsonParams, Map.class);
		return paramsMap;
	}
}
