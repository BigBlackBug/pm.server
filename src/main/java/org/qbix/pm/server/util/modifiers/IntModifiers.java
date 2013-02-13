package org.qbix.pm.server.util.modifiers;

import org.qbix.pm.server.util.modifiers.integer.AbstractIntModifier;
import org.qbix.pm.server.util.modifiers.integer.EqIntModifier;
import org.qbix.pm.server.util.modifiers.integer.GtIntModifier;
import org.qbix.pm.server.util.modifiers.integer.GteIntModifier;
import org.qbix.pm.server.util.modifiers.integer.LtIntModifier;
import org.qbix.pm.server.util.modifiers.integer.LteIntModifier;

public enum IntModifiers {
	EQ("$eq" ,new EqIntModifier()),
	GT("$gt", new GtIntModifier()),
	GTE("$gte", new GteIntModifier()),
	LT("$lt", new LtIntModifier()),
	LTE("$lte", new LteIntModifier());
	
	private String id;
	private AbstractIntModifier modifier;

		
	private IntModifiers(String id, AbstractIntModifier modifier) {
		this.id = id;
		this.modifier = modifier;
	}

	public boolean satisfies(int source, int target){
		return modifier.satisfies(source, target);
	}
	
	public boolean satisfies(int source, int target, int threshold){
		return modifier.satisfies(source, target, threshold);
	}
	
	public static IntModifiers getValueOf(String id) throws IllegalArgumentException{
		int i;
		IntModifiers[] values = IntModifiers.values();
		for (i = 0; i < values.length; i++) {
			IntModifiers value=values[i];
			if(value.id.equals(id)){
				return value;
			}
		}
		if(i==values.length){
			throw new IllegalArgumentException();
		}
		return null;
	}
	
}
