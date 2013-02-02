package org.qbix.pm.server.polling;

import org.qbix.pm.server.exceptions.PMPollingException;

public abstract class AbstractPoller<T extends PollingResult, P extends PollingParams> {

	public abstract T poll(P params) throws PMPollingException; 
	
}
