package org.qbix.pm.server.util.collection.predicates;

public class BooleanPredicate implements Predicate<String>{

	public boolean isSatisfiedBy(String entity) {
		return entity.equalsIgnoreCase("true")
				|| entity.equalsIgnoreCase("false")
				|| entity.equals("1") || entity.equals("0");
	}
	
}