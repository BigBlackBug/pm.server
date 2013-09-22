package org.qbix.pm.server.dto;

import java.io.Serializable;


//json obj
/** <code>
 *  {
	"sessid" : 89, 
	"accountid" : 1,
	} 
 * </code> */
public class UserJoinDTO implements Serializable {

	private static final long serialVersionUID = 7025917910203033553L;

	private Long gameId;

	private Long accountId;

	public UserJoinDTO() {
	}

	public void setGameId(Long sessid) {
		this.gameId = sessid;
	}

	public Long getGameId() {
		return gameId;
	}
	
	public void setAccountId(Long accountid) {
		this.accountId = accountid;
	}
	
	public Long getAccountid() {
		return accountId;
	}
	
}
