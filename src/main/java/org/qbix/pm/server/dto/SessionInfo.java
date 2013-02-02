package org.qbix.pm.server.dto;

import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.util.ParsingUtils;

//json obj
/**
 * <code>
 * {
	"sessid" : 1,
	
	"type" : "lol",
	
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
 */
public class SessionInfo extends AbstractInfo<Session> {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long sessid;

	/** "LOL" , "HON" */
	private String type;
	
	private PlayersValidationInfo pv;

	private ResolveResultCriteriaInfo rrc;

	public SessionInfo() {
	}
	
	public SessionInfo(Long id){
		sessid = id;
	}

	public void setSessid(Long sessid) {
		this.sessid = sessid;
	}

	public Long getSessid() {
		return sessid;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public void setPv(PlayersValidationInfo pv) {
		this.pv = pv;
	}

	public PlayersValidationInfo getPv() {
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
		Session sess = new Session();
		sess.setId(sessid);
		sess.setStatus(SessionStatus.NOT_EXIST);
		sess.setType(ParsingUtils.getSessTypeByString(type));
		
		if (pv != null) {
			sess.setPlayersValidation(pv.convertToEntity());
		}
		if (rrc != null) {
			sess.setResolveResultCriteria(rrc.convertToEntity());
		}
		return sess;
	}

}
