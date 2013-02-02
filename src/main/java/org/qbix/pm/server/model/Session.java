package org.qbix.pm.server.model;

import java.util.List;

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
	private SessionStatus status = SessionStatus.NOT_EXIST;
	
	@Enumerated(EnumType.STRING)
	private SessionType type;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private PlayersValidation playersValidation;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private ResolveResultCriteria resolveResultCriteria;

	@OneToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name = "session_id")
	@OrderBy("timestamp")
	private List<SessionLifeCycleEntry> lifeCycleEntries;
	
	/** For optimistic locking */
	@Version
	private Long version;

	public Session() {
	}

	public void setPlayersValidation(PlayersValidation playerValidation) {
		this.playersValidation = playerValidation;
	}

	public PlayersValidation getPlayersValidation() {
		return playersValidation;
	}

	public void setResolveResultCriteria(
			ResolveResultCriteria resolveResultCriteria) {
		this.resolveResultCriteria = resolveResultCriteria;
	}

	public ResolveResultCriteria getResolveResultCriteria() {
		return resolveResultCriteria;
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

}
