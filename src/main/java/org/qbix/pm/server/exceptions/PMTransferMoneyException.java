package org.qbix.pm.server.exceptions;

public class PMTransferMoneyException extends PMLifecycleException {

	private static final long serialVersionUID = 1L;

	public PMTransferMoneyException() {
		super();
	}

	public PMTransferMoneyException(String mess) {
		super(mess);
	}

	public PMTransferMoneyException(Throwable cause) {
		super(cause);
	}

	public PMTransferMoneyException(String mess, Throwable cause) {
		super(mess, cause);
	}
}
