package org.qbix.pm.server.money;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class SimpleMoneyTransferInfo implements Serializable {

	private static final long serialVersionUID = 4900933241216514994L;

	private final Long gameId;

	/** UserAccount.Id - amontToTransfer */
	private Map<Long, BigDecimal> transferDetails = new HashMap<Long, BigDecimal>();

	public SimpleMoneyTransferInfo(Long gameId) {
		this.gameId = gameId;
	}

	public Long getGameId() {
		return gameId;
	}

	public void setTransferDetails(Map<Long, BigDecimal> transferDetails) {
		this.transferDetails = transferDetails;
	}

	public Map<Long, BigDecimal> getTransferDetails() {
		return transferDetails;
	}

}
