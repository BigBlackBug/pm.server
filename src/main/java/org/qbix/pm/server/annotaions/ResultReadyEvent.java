package org.qbix.pm.server.annotaions;

public class ResultReadyEvent {

	public final Long resultId;
	
	public ResultReadyEvent(Long aSessId) {
		resultId = aSessId;
	}
	
}
