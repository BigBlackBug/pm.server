package org.qbix.pm.server.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionType;

//json obj
/**
 * <code>
 * {
	"sessid" : 1, 
	
	"type" : "hon",
	
	"stake" : 100.0, 

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

	private VictoryCriteriaInfo vc;

	private BigDecimal stake;

	private List<PlayerEntryInfo> playerEntries;

	private Integer status;
	
	public SessionInfo() {
	}

	public SessionInfo(Long id) {
		sessid = id;
	}

	public void setSessid(Long sessionId) {
		this.sessid = sessionId;
	}

	public Long getSessid() {
		return sessid;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getStatus() {
		return status;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setVc(VictoryCriteriaInfo vc) {
		this.vc = vc;
	}

	public VictoryCriteriaInfo getVc() {
		return vc;
	}

	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}

	public BigDecimal getStake() {
		return stake;
	}

	public void setPlayerEntries(List<PlayerEntryInfo> playerInfos) {
		this.playerEntries = playerInfos;
	}

	public List<PlayerEntryInfo> getPlayerEntries() {
		return playerEntries;
	}

	@Override
	public Session convertToEntity(EntityManager em) {
		Session sess = new Session();
		if(sessid != null){
			sess.setId(sessid);
		}
		if (type != null) {
			sess.setType(SessionType.getSessionType(type));
		}

		if (stake != null) {
			sess.setStake(stake);
		}

		if (playerEntries != null) {
			for (PlayerEntryInfo pei : playerEntries) {
				PlayerEntry pe = pei.convertToEntity(em);
				pe.setSession(sess);
				sess.getPlayers().add(pe);
			}
		}

		if (vc != null) {
			sess.setVictoryCriteria(vc.convertToEntity(em));
		}
		return sess;
	}

}
