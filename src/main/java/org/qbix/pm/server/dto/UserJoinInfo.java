package org.qbix.pm.server.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


//json obj
/** <code>
 *  {
	"sessid" : 89, 
	"accountid" : 1,
    "team" : 0
	} 
 * </code> */
public class UserJoinInfo implements Serializable {

	private static final long serialVersionUID = 7025917910203033553L;

	private Long sessid;

	private Long accountid;
	
	private Integer team;
	
	private Map<String, Object> params = new HashMap<String, Object>();

	public UserJoinInfo() {
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

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public Map<String, Object> getParams() {
		return params;
	}

	public void setTeam(Integer team) {
		this.team = team;
	}
	
	public Integer getTeam() {
		return team;
	}
	
}
