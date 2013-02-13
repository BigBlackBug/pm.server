package org.qbix.pm.server.beans;

import javax.ejb.Local;

import org.qbix.pm.server.exceptions.PMException;

@Local
public interface SessionFacade extends ClientAPI {

	public void resolveResult(Long resultId) throws PMException;
	
}
