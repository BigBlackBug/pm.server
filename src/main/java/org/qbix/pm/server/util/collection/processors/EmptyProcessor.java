package org.qbix.pm.server.util.collection.processors;

public class EmptyProcessor<T> implements Processor<T>{

	public T process(T entity) {
		return entity;
	}

}
