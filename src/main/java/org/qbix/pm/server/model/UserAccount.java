package org.qbix.pm.server.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class UserAccount extends AbstractEntity {

	private static final long serialVersionUID = 2196558275988715559L;
	
	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private LoLAccount lolAccount;

	private BigDecimal balance;

	public UserAccount() {
	}

	public LoLAccount getLoLAccount() {
		return lolAccount;
	}

	public void setLoLAccount(LoLAccount lolAccount) {
		this.lolAccount = lolAccount;
	}

	public void setBalance(BigDecimal currency) {
		this.balance = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

}
