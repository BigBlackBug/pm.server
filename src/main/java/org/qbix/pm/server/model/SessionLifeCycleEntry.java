package org.qbix.pm.server.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class SessionLifeCycleEntry extends AbstractEntity {

	private static final long serialVersionUID = -3615255025713052880L;

	@ManyToOne
	@NotNull
	private Session session;
	
	private SessionStatus status;
	
	public void setSession(Session session) {
		this.session = session;
	}
	
	public Session getSession() {
		return session;
	}
	
	public void setStatus(SessionStatus status) {
		this.status = status;
	}
	
	public SessionStatus getStatus() {
		return status;
	}
	
}
