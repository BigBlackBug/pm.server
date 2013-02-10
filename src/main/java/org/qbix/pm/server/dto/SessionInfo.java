package org.qbix.pm.server.dto;

import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.model.SessionType;

//json obj
/**
 * <code>
 * {
	"sessid" : 1, 
	
	"type" : "hon",

	"pr" : { "parserId" : 1, "requirements" : { "key1" : "val1" } },
	
	"vc" : { "parserId" : 10, "criteria" : { "key1" : 2 } }
	
	} </code>
 * 
 */
public class SessionInfo extends AbstractInfo<Session> {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long sessid;

	/** "LOL" , "HON" */
	private String type;
	
	private PlayerRequirementsInfo pr;

	private VictoryCriteriaInfo vc;

	public SessionInfo() {
	}
	
	public SessionInfo(Long id){
		sessid = id;
	}

	public void setSessid(Long sessionId) {
		this.sessid = sessionId;
	}

	public Long getSessionId() {
		return sessid;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public void setPr(PlayerRequirementsInfo playerRequirements) {
		this.pr = playerRequirements;
	}

	public PlayerRequirementsInfo getPr() {
		return pr;
	}

	public void setVc(VictoryCriteriaInfo vc) {
		this.vc = vc;
	}

	public VictoryCriteriaInfo getVc() {
		return vc;
	}

	@Override
	public Session convertToEntity() {
		Session sess = new Session();
		sess.setId(sessid);
		sess.setStatus(SessionStatus.DOES_NOT_EXIST);
		sess.setType(SessionType.getSessionType(type));
		
		if (pr != null) {
			sess.setPlayerRequirements(pr.convertToEntity());
		}
		if (vc != null) {
			sess.setVictoryCriteria(vc.convertToEntity());
		}
		return sess;
	}

}
