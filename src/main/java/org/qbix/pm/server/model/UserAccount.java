package org.qbix.pm.server.model;

import java.math.BigDecimal;
import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;

@Entity
public class UserAccount extends AbstractEntity {

	private static final long serialVersionUID = 2196558275988715559L;
	
	public static enum GameAccountType {
		HON, LOL
	}

	/** GameType - AccId map */
	@ElementCollection
	private Map<GameAccountType, Long> gamesAccounts;

	private BigDecimal currency;
	
	public UserAccount() {
	}

	public void setGamesAccounts(Map<GameAccountType, Long> gamesAccounts) {
		this.gamesAccounts = gamesAccounts;
	}

	public Map<GameAccountType, Long> getGamesAccounts() {
		return gamesAccounts;
	}
	
	public void setCurrency(BigDecimal currency) {
		this.currency = currency;
	}
	
	public BigDecimal getCurrency() {
		return currency;
	}

}
