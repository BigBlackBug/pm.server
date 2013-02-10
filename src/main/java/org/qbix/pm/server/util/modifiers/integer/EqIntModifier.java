package org.qbix.pm.server.util.modifiers.integer;

public class EqIntModifier extends AbstractIntModifier{

	@Override
	public boolean satisfies(Integer source, Integer target) {
		return source == target;
	}

}
