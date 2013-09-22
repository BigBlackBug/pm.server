package org.qbix.pm.server.model;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.google.gson.Gson;

@MappedSuperclass
public abstract class EntityWithSerializedParams extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Lob
	@Basic(fetch = FetchType.EAGER)
	@Column(name = "json_params")
	protected String jsonParams;

	private transient Map<String, Object> paramsMap;

	private transient volatile boolean needToConvertMapParams = false;

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

		/*
		 * Если мы получали карту, то возможно мы в нее что то записывали, и
		 * поэтому при следующем персисте/апдейте мы должны конвертить ее в
		 * json, чтобы записать в бд новые данные.
		 * 
		 * TODO подумать как решить проблему лишних конвертов, когда мы просто
		 * получаем карту, но ничего в ней не меняем
		 */
		needToConvertMapParams = true;

		if ((getJsonParams() == null) || getJsonParams().isEmpty()) {
			paramsMap = new HashMap<String, Object>();
			return paramsMap;
		}

		paramsMap = new Gson().fromJson(jsonParams, Map.class);

		return paramsMap;
	}

	void convertMapAndSetJSParams() {
		jsonParams = new Gson().toJson(getParamsMap());
	}

	@PrePersist
	protected void convertParamsPP() {
		if (needToConvertMapParams) {
			convertMapAndSetJSParams();
		}
	}

	@PreUpdate
	protected void convertParamsPU() {
		if (needToConvertMapParams) {
			convertMapAndSetJSParams();
		}
	}

}
