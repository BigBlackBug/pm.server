package org.qbix.pm.server.dto;

import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.util.ParsingUtils;

//json obj
/**
 * <code>
 * {
	"sess_id" : 1, // i think we don't need session_id here.
	
	"type" : "hon",

	player_requirements : {},
	
	victory_criteria : {}
	
	} </code>
 * 
 */
public class SessionInfo extends AbstractInfo<Session> {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long sessionId;

	/** "LOL" , "HON" */
	private String type;
	
	private PlayerRequirementsInfo playerRequirements;

	private VictoryCriteriaInfo victoryCriteria;

	public SessionInfo() {
	}
	
	public SessionInfo(Long id){
		sessionId = id;
	}

	public void setSessid(Long sessionId) {
		this.sessionId = sessionId;
	}

	public Long getSessionId() {
		return sessionId;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getType() {
		return type;
	}

	public void setPlayerRequirements(PlayerRequirementsInfo playerRequirements) {
		this.playerRequirements = playerRequirements;
	}

	public PlayerRequirementsInfo getPplayerRequirements() {
		return playerRequirements;
	}

	public void setVictoryCriteria(VictoryCriteriaInfo vc) {
		this.victoryCriteria = vc;
	}

	public VictoryCriteriaInfo getVictoryCriteria() {
		return victoryCriteria;
	}

	@Override
	public Session convertToEntity() {
		Session sess = new Session();
		sess.setId(sessionId);
		sess.setStatus(SessionStatus.NOT_EXIST);
		sess.setType(ParsingUtils.getSessTypeByString(type));
		
		if (playerRequirements != null) {
			sess.setPlayerRequirements(playerRequirements.convertToEntity());
		}
		if (victoryCriteria != null) {
			sess.setVictoryCriteria(victoryCriteria.convertToEntity());
		}
		return sess;
	}

}
