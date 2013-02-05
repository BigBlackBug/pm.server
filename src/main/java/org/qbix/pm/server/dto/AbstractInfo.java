package org.qbix.pm.server.dto;

import java.io.Serializable;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public abstract class AbstractInfo<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public abstract T convertToEntity();

	protected byte[] serializeMapParams(Map<String, Object> map){
		Kryo k = new Kryo();
		Output out = new Output(512, 4096);
		k.writeObject(out, map);
		out.close();
		return out.toBytes();
	}
}
