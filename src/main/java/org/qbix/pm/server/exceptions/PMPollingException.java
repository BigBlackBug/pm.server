package org.qbix.pm.server.exceptions;

public class PMPollingException extends PMLifecycleException {
	
	private static final long serialVersionUID = 1L;

	public PMPollingException() {
		super();
	}

	public PMPollingException(String mess) {
		super(mess);
	}

	public PMPollingException(Throwable cause){
		super(cause);
	}
	
	public PMPollingException(String mess, Throwable cause) {
		super(mess, cause);
	}
	
}
