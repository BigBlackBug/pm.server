package org.qbix.pm.server.util.collection.returnfilters;

public interface ReturnFilter<P, R> {
	public R returns(P entity);
}