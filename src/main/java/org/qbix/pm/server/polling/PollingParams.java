package org.qbix.pm.server.polling;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.qbix.pm.server.model.AbstractEntity;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.VictoryCriteria;

@Entity
//Should be persisted!
public abstract class PollingParams extends AbstractEntity{
	
	private static final long serialVersionUID = 2385457736703063311L;
	
	@OneToOne
	private Session session;
	 
	public Session getSession() {
		return session;
	}
	
	public void setSession(Session session) {
		this.session = session;
	}
	
}
