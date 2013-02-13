package org.qbix.pm.server.exceptions;

public class PMParsingException extends PMLifecycleException {
	
	private static final long serialVersionUID = 1L;

	public PMParsingException() {
		super();
	}

	public PMParsingException(String mess) {
		super(mess);
	}
	
	public PMParsingException(String message, int returnCode){
		super(message);
		setCode(returnCode);
	}

	public PMParsingException(Throwable cause){
		super(cause);
	}
	
	public PMParsingException(String mess, Throwable cause) {
		super(mess, cause);
	}
	
}