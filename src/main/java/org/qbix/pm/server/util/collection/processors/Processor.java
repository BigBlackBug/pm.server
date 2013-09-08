package org.qbix.pm.server.util.collection.processors;

public interface Processor<T> {
	public T process(T entity);
}
