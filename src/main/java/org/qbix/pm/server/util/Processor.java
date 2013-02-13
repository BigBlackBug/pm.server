package org.qbix.pm.server.util;

public interface Processor<T> {
	public T process(T entity);
}
