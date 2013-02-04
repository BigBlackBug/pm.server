package org.qbix.pm.server.polling;

import javax.ejb.Stateless;

import org.qbix.pm.server.exceptions.PMPollingException;

@Stateless
public abstract class AbstractPoller<T extends PollingResult, P extends PollingParams> {

	public abstract T poll(P params) throws PMPollingException; 
	
}
