package org.qbix.pm.server.polling;

import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.annotaions.Traceble;
import org.qbix.pm.server.beans.AbstractBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Startup
public class PollingScheduler extends AbstractBean {

	private static Logger log = LoggerFactory.getLogger(PollingScheduler.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;
	
	@Traceble
	@Schedule(dayOfWeek = "*", hour = "*", minute = "*/2", second = "0", persistent = false)
	public void poll() {
		log.info("polling started");
	}
	
}
