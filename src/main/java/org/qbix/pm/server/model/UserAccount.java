package org.qbix.pm.server.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class UserAccount extends AbstractEntity {

	private static final long serialVersionUID = 2196558275988715559L;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name="lolacc_id")
	private LoLAccount lolAccount;

	private BigDecimal balance = new BigDecimal(0);
	
	private BigDecimal inGameCash = new BigDecimal(0);

	private String nickName;

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public BigDecimal getInGameCash() {
		return inGameCash;
	}
	
	public void setInGameCash(BigDecimal inGameCash) {
		this.inGameCash = inGameCash;
	}

}
