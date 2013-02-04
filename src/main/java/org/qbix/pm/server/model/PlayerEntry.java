package org.qbix.pm.server.model;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class PlayerEntry {
	
	@ManyToOne
	private Session session;
	
	@Enumerated
	private Team team;
	
	@OneToOne
	private UserAccount account;
	
}
