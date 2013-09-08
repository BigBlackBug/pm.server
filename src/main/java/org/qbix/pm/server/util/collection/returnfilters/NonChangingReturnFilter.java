package org.qbix.pm.server.util.collection.returnfilters;

public class NonChangingReturnFilter<T> implements ReturnFilter<T, T> {

	public T returns(T entity) {
		return entity;
	}

}
