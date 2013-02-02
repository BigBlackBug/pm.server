package org.qbix.pm.server.exceptions;

public class PMValidationException extends PMException {

	private static final long serialVersionUID = 1L;

	public PMValidationException() {
		super();
	}

	public PMValidationException(String mess) {
		super(mess);
	}

	public PMValidationException(Throwable cause){
		super(cause);
	}

	public PMValidationException(String mess, Throwable cause) {
		super(mess, cause);
	}

}
