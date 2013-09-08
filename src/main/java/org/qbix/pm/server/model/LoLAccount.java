package org.qbix.pm.server.model;

import javax.persistence.Entity;

@Entity
public class LoLAccount extends AbstractEntity {

	private static final long serialVersionUID = 8144389549745202954L;

	private String summonerInternalName;

	private long accountID;

	public String getSummonerInternalName() {
		return summonerInternalName;
	}

	public void setSummonerInternalName(String summonerInternalName) {
		this.summonerInternalName = summonerInternalName;
	}

	public long getAccountID() {
		return accountID;
	}

	public void setAccountID(long accountID) {
		this.accountID = accountID;
	}

}
