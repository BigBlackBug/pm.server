package org.qbix.pm.server.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class PlayerEntry extends AbstractEntity {

	private static final long serialVersionUID = -7655206966941068335L;

	@ManyToOne
	private Session session;
	
	@Enumerated
	private SessionTeam team;
	
	@OneToOne
	private UserAccount account;

	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public SessionTeam getTeam() {
		return team;
	}

	public void setTeam(SessionTeam team) {
		this.team = team;
	}

	public UserAccount getAccount() {
		return account;
	}

	public void setAccount(UserAccount account) {
		this.account = account;
	}
	
	
}
