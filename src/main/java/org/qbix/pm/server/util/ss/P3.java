package org.qbix.pm.server.util.ss;

public class P3<F, S, T> extends P<F, S> {

	public final T third;

	public P3(F aF, S aS, T aT) {
		super(aF, aS);
		third = aT;
	}
}
