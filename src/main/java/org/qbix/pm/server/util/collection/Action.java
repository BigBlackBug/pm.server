package org.qbix.pm.server.util.collection;

public interface Action<R, P> {
	public R execute(P... params);
}