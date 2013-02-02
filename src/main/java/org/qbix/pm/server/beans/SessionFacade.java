package org.qbix.pm.server.beans;

import javax.ejb.Local;

import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.polling.PollingResult;

@Local
public interface SessionFacade extends ClientAPI {

	public void resolveResult(PollingResult pr) throws PMLifecycleException;
	
}
