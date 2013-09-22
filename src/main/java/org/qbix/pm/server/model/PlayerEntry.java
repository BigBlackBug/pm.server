package org.qbix.pm.server.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
public class PlayerEntry extends AbstractEntity {

	private static final long serialVersionUID = -7655206966941068335L;

	@ManyToOne
	private Game game;
	
	@Enumerated
	private Team team;
	
	@ManyToOne(fetch = FetchType.EAGER)
	private UserAccount account;
	
	/** if not -1 -> user confirmed participation */
	private BigDecimal stake = new BigDecimal(-1);

	public Game getGame() {
		return game;
	}

	public void setGame(Game g) {
		this.game = g;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}
	
	public BigDecimal getStake() {
		return stake;
	}
	
	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}
	
}
