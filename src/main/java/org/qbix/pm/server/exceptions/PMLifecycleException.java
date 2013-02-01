package org.qbix.pm.server.exceptions;

public class PMLifecycleException extends PMException {
	
	private static final long serialVersionUID = 1L;

	public PMLifecycleException() {
		super();
	}

	public PMLifecycleException(String mess) {
		super(mess);
	}

	public PMLifecycleException(String mess, Throwable cause) {
		super(mess, cause);
	}
}
