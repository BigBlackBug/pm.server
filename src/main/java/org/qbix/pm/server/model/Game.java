package org.qbix.pm.server.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;

@Entity
public class Game extends AbstractEntity {

	private static final long serialVersionUID = 414967553274661010L;

	@Enumerated(EnumType.STRING)
	private GameStatus status = GameStatus.DOES_NOT_EXIST;

	@Enumerated(EnumType.STRING)
	private GameType type;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private VictoryCriteria victoryCriteria;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "game_id")
	@OrderBy("creationDate")
	private List<GameLifeCycleEntry> lifeCycleEntries = new ArrayList<GameLifeCycleEntry>();

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL , orphanRemoval = true)
	@OrderBy("creationDate")
	private List<PlayerEntry> players = new ArrayList<PlayerEntry>();

	@OneToOne
	private UserAccount creator;
	
	private BigDecimal stake;

	public Game() {
	}

	public void setVictoryCriteria(VictoryCriteria victoryCriteria) {
		this.victoryCriteria = victoryCriteria;
	}

	public VictoryCriteria getVictoryCriteria() {
		return victoryCriteria;
	}

	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public GameStatus getStatus() {
		return status;
	}

	public void setType(GameType type) {
		this.type = type;
	}

	public GameType getType() {
		return type;
	}

	public void setLifeCycleEntries(List<GameLifeCycleEntry> lifeCycleEntries) {
		this.lifeCycleEntries = lifeCycleEntries;
	}

	public List<GameLifeCycleEntry> getLifeCycleEntries() {
		return lifeCycleEntries;
	}

	public List<PlayerEntry> getPlayers() {
		return players;
	}

	public void setPlayers(List<PlayerEntry> players) {
		this.players = players;
	}

	public BigDecimal getStake() {
		return stake;
	}

	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}
	
	public UserAccount getCreator() {
		return creator;
	}
	
	public void setCreator(UserAccount creator) {
		this.creator = creator;
	}

}
