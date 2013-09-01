package org.qbix.pm.server.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
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
import javax.persistence.Version;

@Entity
public class Session extends AbstractEntity {

	private static final long serialVersionUID = 414967553274661010L;

	@Enumerated(EnumType.STRING)
	private SessionStatus status = SessionStatus.DOES_NOT_EXIST;

	@Enumerated(EnumType.STRING)
	private SessionType type;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private VictoryCriteria victoryCriteria;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "session_id")
	@OrderBy("creationDate")
	private List<SessionLifeCycleEntry> lifeCycleEntries = new ArrayList<SessionLifeCycleEntry>();

	@OneToMany(cascade = { CascadeType.REMOVE })
	@JoinColumn(name = "session_id")
	private List<ScheduledTaskLog> pollingLogs = new ArrayList<ScheduledTaskLog>();

	@OneToMany(mappedBy = "session", cascade = CascadeType.ALL )
	//TODO add OptimisticLock exclusion !!! 
	private Set<PlayerEntry> players = new HashSet<PlayerEntry>();

	@OneToOne
	private UserAccount initiator;
	
	private BigDecimal stake;

	/** For optimistic locking */
	@Version
	private Long version;

	public Session() {
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

	public List<ScheduledTaskLog> getPollingLogs() {
		return pollingLogs;
	}

	public void setPollingLogs(List<ScheduledTaskLog> pollingLogs) {
		this.pollingLogs = pollingLogs;
	}

	public BigDecimal getStake() {
		return stake;
	}

	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}
	
	public UserAccount getInitiator() {
		return initiator;
	}
	
	public void setInitiator(UserAccount initiator) {
		this.initiator = initiator;
	}

}
