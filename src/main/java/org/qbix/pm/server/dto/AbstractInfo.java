package org.qbix.pm.server.dto;

import java.io.Serializable;

import javax.persistence.EntityManager;

public abstract class AbstractInfo<T> implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public abstract T convertToEntity(EntityManager em);
}
