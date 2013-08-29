package org.qbix.pm.server.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class PMException extends Exception {

	private static final long serialVersionUID = 1L;

	protected int code = 0;

	public PMException() {
		super();
	}

	public PMException(String mess) {
		super(mess);
	}

	public PMException(Throwable cause) {
		super(cause);
	}

	public PMException(String mess, Throwable cause) {
		super(mess, cause);
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

}
