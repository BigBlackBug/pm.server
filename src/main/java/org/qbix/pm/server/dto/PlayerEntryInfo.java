package org.qbix.pm.server.dto;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.UserAccount;

public class PlayerEntryInfo extends AbstractInfo<PlayerEntry>{

	private static final long serialVersionUID = -1957412849571608071L;

	/** 0/1 */
	private int team;
	
	private Long accountId;
	
	private BigDecimal stake = new BigDecimal(-1);

	public PlayerEntryInfo() {
		// TODO Auto-generated constructor stub
	}
	
	public Long getAccountId() {
		return accountId;
	}
	
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	
	public BigDecimal getStake() {
		return stake;
	}
	
	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}
	
	public int getTeam() {
		return team;
	}
	
	public void setTeam(int team) {
		this.team = team;
	}
	
	@Override
	public PlayerEntry convertToEntity(EntityManager em) {
		PlayerEntry pe = new PlayerEntry();
		pe.setAccount(em.getReference(UserAccount.class, accountId));
		pe.setStake(stake);
		pe.setTeam(SessionTeam.valueOf(team));
		//setSession required
		return pe;
	}
	

}
