package org.qbix.pm.server.intercept;

public class StartSessionEvent {

	public final Long sessionId;
	
	public StartSessionEvent(Long sessId) {
		sessionId = sessId;
	}
	
}
