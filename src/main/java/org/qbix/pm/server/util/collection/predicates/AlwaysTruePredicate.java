package org.qbix.pm.server.util.collection.predicates;

public class AlwaysTruePredicate<T> implements Predicate<T>{

	public boolean isSatisfiedBy(T entity) {
		return true;
	}
	
}