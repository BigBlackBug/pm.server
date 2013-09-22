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

	private Long sessid;

	private Long accountid;

	public UserJoinDTO() {
	}

	public void setSessid(Long sessid) {
		this.sessid = sessid;
	}

	public Long getSessid() {
		return sessid;
	}
	
	public void setAccountid(Long accountid) {
		this.accountid = accountid;
	}
	
	public Long getAccountid() {
		return accountid;
	}
	
}
