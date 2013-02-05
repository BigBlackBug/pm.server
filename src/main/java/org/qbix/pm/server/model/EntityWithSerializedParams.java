package org.qbix.pm.server.model;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

@MappedSuperclass
public abstract class EntityWithSerializedParams extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	@Lob
	protected byte[] parameters;

	private transient Map<String, Object> paramsMap;

	public void setParameters(byte[] parameters) {
		this.parameters = parameters;
	}

	public byte[] getParameters() {
		return parameters;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> getParamsMap() {

		if (paramsMap != null) {
			return paramsMap;
		}

		Kryo k = new Kryo();
		paramsMap = (Map<String, Object>) k.readObject(new Input(parameters),
				LinkedHashMap.class);

		return paramsMap;
	}
}
