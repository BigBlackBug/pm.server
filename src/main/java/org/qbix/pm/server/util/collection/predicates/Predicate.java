package org.qbix.pm.server.util.collection.predicates;

public interface Predicate<T> {

	public boolean isSatisfiedBy(T entity);

}
