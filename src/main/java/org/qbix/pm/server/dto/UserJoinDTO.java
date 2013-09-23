package org.qbix.pm.server.dto;

import java.io.Serializable;

/**
  <code>
    {"gameId" : 89, "accountId" : 1} 
  </code> 
 */
public class UserJoinDTO implements Serializable {

	private static final long serialVersionUID = 7025917910203033553L;

	private Long gameId;

	private Long accountId;

	public UserJoinDTO() {
	}

	public void setGameId(Long gameID) {
		this.gameId = gameID;
	}

	public Long getGameId() {
		return gameId;
	}
	
	public void setAccountId(Long accountID) {
		this.accountId = accountID;
	}
	
	public Long getAccountId() {
		return accountId;
	}
	
}
