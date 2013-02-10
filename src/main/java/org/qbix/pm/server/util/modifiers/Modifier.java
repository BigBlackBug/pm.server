package org.qbix.pm.server.util.modifiers;

public interface Modifier<T> {
	public boolean satisfies(T source, T target);
}
