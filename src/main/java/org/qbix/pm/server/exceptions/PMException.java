package org.qbix.pm.server.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class PMException extends Exception {

	private static final long serialVersionUID = 1L;

	public PMException() {
		super();
	}

	public PMException(String mess) {
		super(mess);
	}
	
	public PMException(Throwable cause){
		super(cause);
	}

	public PMException(String mess, Throwable cause) {
		super(mess, cause);
	}
}
