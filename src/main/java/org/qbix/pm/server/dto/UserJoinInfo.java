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

	public void setSessid(Long sessid) {
		this.sessid = sessid;
	}

	public Long getSessid() {
		return sessid;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public Map<String, String> getParams() {
		return params;
	}

}
