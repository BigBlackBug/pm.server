package org.qbix.pm.server.polling;

import java.util.Map;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.qbix.pm.server.model.AbstractEntity;
import org.qbix.pm.server.model.Session;

@Entity
public class PollingParams extends AbstractEntity {

	private static final long serialVersionUID = 2211425246380582907L;
	
	@OneToOne
	private Session session;
	
	private long pollerId;
	
	@ElementCollection
	private Map<String, String> mapParams;

	public void setSession(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}
	
	public long getPollerId() {
		return pollerId;
	}
	
	public void setPollerId(long pollerId) {
		this.pollerId = pollerId;
	}
	
	public void setMapParams(Map<String, String> mapParams) {
		this.mapParams = mapParams;
	}
	
	public Map<String, String> getMapParams() {
		return mapParams;
	}
}
