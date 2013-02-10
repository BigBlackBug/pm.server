package org.qbix.pm.server.model;

import javax.persistence.Entity;

@Entity
public class UserAccount extends AbstractEntity {

	private static final long serialVersionUID = 2196558275988715559L;
	
	private LoLAccountInfo lolAccountInfo;

	public LoLAccountInfo getLoLAccountInfo() {
		return lolAccountInfo;
	}

	public void setLoLAccountInfo(LoLAccountInfo lolAccountInfo) {
		this.lolAccountInfo = lolAccountInfo;
	} 

}
