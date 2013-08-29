package org.qbix.pm.server.exceptions;


public class PMMoneyException extends PMException {

	private static final long serialVersionUID = 7223661789236306947L;

	public PMMoneyException() {
		super();
	}

	public PMMoneyException(String mess) {
		super(mess);
	}

	public PMMoneyException(Throwable cause) {
		super(cause);
	}

	public PMMoneyException(String mess, Throwable cause) {
		super(mess, cause);
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
}
