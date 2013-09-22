package org.qbix.pm.server.dto;

import java.math.BigDecimal;

import javax.persistence.EntityManager;

import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Team;
import org.qbix.pm.server.model.UserAccount;

public class PlayerEntryDTO extends AbstractDTO<PlayerEntry>{

	private static final long serialVersionUID = -1957412849571608071L;

	/** 0/1 */
	private int team;
	
	private Long accountId;

	public PlayerEntryDTO() {
	}
	
	public Long getAccountId() {
		return accountId;
	}
	
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
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
		pe.setStake(new BigDecimal(-1));
		pe.setTeam(Team.valueOf(team));
		//setGame required
		return pe;
	}
	

}
