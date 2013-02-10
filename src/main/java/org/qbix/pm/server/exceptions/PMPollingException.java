package org.qbix.pm.server.exceptions;

import org.qbix.pm.server.polling.ReturnCode;

public class PMPollingException extends PMLifecycleException {
	
	private static final long serialVersionUID = 1L;

	public PMPollingException() {
		super();
	}

	public PMPollingException(String mess) {
		super(mess);
	}
	
	public PMPollingException(String message, int returnCode){
		super(message);
		setCode(returnCode);
	}
	
	public PMPollingException(String message, ReturnCode returnCode){
		super(message);
		setCode(returnCode.getCode());
	}

	public PMPollingException(Throwable cause){
		super(cause);
	}
	
	public PMPollingException(String mess, Throwable cause) {
		super(mess, cause);
	}
	
}
