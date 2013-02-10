package org.qbix.pm.server.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SimpleMoneyTransferInfo implements Serializable {

	private static final long serialVersionUID = 4900933241216514994L;

	private Long sessionId;

	/** UserAccount.Id - amontToTransfer */
	private Map<Long, BigDecimal> transferDetails = new HashMap<Long, BigDecimal>();

	public SimpleMoneyTransferInfo() {
	}

	public SimpleMoneyTransferInfo(Long aSessId) {
		sessionId = aSessId;
	}

	public void setSessionId(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getSessionId() {
		return sessionId;
	}

	public void setTransferDetails(Map<Long, BigDecimal> transferDetails) {
		this.transferDetails = transferDetails;
	}

	public Map<Long, BigDecimal> getTransferDetails() {
		return transferDetails;
	}

}
