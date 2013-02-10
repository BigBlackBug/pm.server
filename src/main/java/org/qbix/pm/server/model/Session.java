package org.qbix.pm.server.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import org.qbix.pm.server.polling.PollingLogEntry;
import org.qbix.pm.server.polling.PollingParams;

@Entity
public class Session extends AbstractEntity {

	private static final long serialVersionUID = 414967553274661010L;

	@Enumerated(EnumType.STRING)
	private SessionStatus status = SessionStatus.DOES_NOT_EXIST;

	@Enumerated(EnumType.STRING)
	private SessionType type;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private PlayerRequirements playerRequirements;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private VictoryCriteria victoryCriteria;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private PollingParams pollingParams;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "session_id")
	@OrderBy("timestamp")
	private List<SessionLifeCycleEntry> lifeCycleEntries;

	@OneToMany(cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "session_id")
	private List<PollingLogEntry> pollingLogs;

	@OneToMany
	//TODO add OptimisticLock exclusion !!! 
	private Set<PlayerEntry> players;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date sessionStartDate;

	private BigDecimal stake;

	/** For optimistic locking */
	@Version
	private Long version;

	public Session() {
	}

	public void setPlayerRequirements(PlayerRequirements playerRequirements) {
		this.playerRequirements = playerRequirements;
	}

	public PlayerRequirements getPlayerRequirements() {
		return playerRequirements;
	}

	public void setVictoryCriteria(VictoryCriteria victoryCriteria) {
		this.victoryCriteria = victoryCriteria;
	}

	public VictoryCriteria getVictoryCriteria() {
		return victoryCriteria;
	}

	public void setStatus(SessionStatus status) {
		this.status = status;
	}

	public SessionStatus getStatus() {
		return status;
	}

	public void setType(SessionType type) {
		this.type = type;
	}

	public SessionType getType() {
		return type;
	}

	public void setLifeCycleEntries(List<SessionLifeCycleEntry> lifeCycleEntries) {
		this.lifeCycleEntries = lifeCycleEntries;
	}

	public List<SessionLifeCycleEntry> getLifeCycleEntries() {
		return lifeCycleEntries;
	}

	public void setPollingParams(PollingParams pollingParams) {
		this.pollingParams = pollingParams;
	}

	public PollingParams getPollingParams() {
		return pollingParams;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Long getVersion() {
		return version;
	}

	public Set<PlayerEntry> getPlayers() {
		return players;
	}

	public void setPlayers(Set<PlayerEntry> players) {
		this.players = players;
	}

	public List<PollingLogEntry> getPollingLogs() {
		return pollingLogs;
	}

	public void setPollingLogs(List<PollingLogEntry> pollingLogs) {
		this.pollingLogs = pollingLogs;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getSessionStartDate() {
		return sessionStartDate;
	}

	public void setSessionStartDate(Date sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}

	public BigDecimal getStake() {
		return stake;
	}

	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}

}
