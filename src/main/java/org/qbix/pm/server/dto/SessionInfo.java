package org.qbix.pm.server.dto;

import org.qbix.pm.server.model.Session;

//json obj
//json obj
/**
* <code>
* {
	"sessid" : 1,
	
	"pv" :  {
			"validator" : 2,
			"params" : { "key1" : "asd" , "key2" : "sdf"}
		},
	
	"rrc" : {
			"resolver" : 333,
			"params" : {"k1" : "ololo", "k2" : "trololo" }
		}
	} </code>
* 
* 
*/
public class SessionInfo extends AbstractInfo<Session> {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long sessid;

	private PlayerValidationInfo pv;

	private ResolveResultCriteriaInfo rrc;

	public SessionInfo() {
	}

	public void setSessid(Long sessid) {
		this.sessid = sessid;
	}
	
	public Long getSessid() {
		return sessid;
	}
	
	public void setPv(PlayerValidationInfo pv) {
		this.pv = pv;
	}
	
	public PlayerValidationInfo getPv() {
		return pv;
	}
	
	public void setRrc(ResolveResultCriteriaInfo rrc) {
		this.rrc = rrc;
	}
	
	public ResolveResultCriteriaInfo getRrc() {
		return rrc;
	}

	@Override
	public Session convertToEntity() {
		// TODO Auto-generated method stub
		return null;
	}

}
