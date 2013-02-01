package org.qbix.pm.server.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

//json obj
public class UserJoinInfo implements Serializable {

	private static final long serialVersionUID = 7025917910203033553L;

	private Long sessid;
	
	private Map<String, String> params = new HashMap<String, String>();
	
	public UserJoinInfo() {
	}
	
	public void setSessionId(Long sessionId) {
		this.sessid = sessionId;
	}
	
	public Long getSessionId() {
		return sessid;
	}
	
	public void setParameters(Map<String, String> parameters) {
		this.params = parameters;
	}
	
	public Map<String, String> getParameters() {
		return params;
	}
	
}
