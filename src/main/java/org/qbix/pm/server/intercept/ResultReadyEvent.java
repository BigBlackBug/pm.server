package org.qbix.pm.server.intercept;

public class ResultReadyEvent {

	public final Long resultId;
	
	public ResultReadyEvent(Long id) {
		resultId = id;
	}
	
}
