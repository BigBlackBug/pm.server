package org.qbix.pm.server.util.modifiers.integer;

public class EqIntModifier extends AbstractIntModifier{

	@Override
	public boolean satisfies(Integer source, Integer target) {
		return source == target;
	}

	@Override
	public boolean satisfies(Integer source, Integer target, Integer threshold) {
		return source >= target - threshold && source <= target + threshold;
	}

}
