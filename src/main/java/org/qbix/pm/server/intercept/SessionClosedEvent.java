package org.qbix.pm.server.intercept;

public class SessionClosedEvent {

	public final Long sessionId;

	public SessionClosedEvent(Long aSessid) {
		sessionId = aSessid;
	}
}
