package org.qbix.pm.server.beans;

import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.qbix.pm.server.intercept.TraceInterceptor;

@Interceptors({TraceInterceptor.class})
public abstract class AbstractBean {
	
	@PersistenceContext(unitName = "pm")
	protected EntityManager em;
	
	protected <T> T lockEntity(Class<T> entityClass, Object id) {
		if (id == null) {
			throw new ValidationException("Entity id cant be null");
		}

		T entity = em.find(entityClass, id, LockModeType.PESSIMISTIC_WRITE);

		if (entity == null) {
			throw new ValidationException("No entity with id = " + id);
		}
		return entity;
	}
	
}
