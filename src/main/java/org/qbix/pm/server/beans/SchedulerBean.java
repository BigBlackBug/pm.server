package org.qbix.pm.server.beans;

import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/** Бин для запуска задач по расписанию */
@Stateless
@Startup
public class SchedulerBean {

//	private static Logger log = LoggerFactory.getLogger(SchedulerBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;
	
	@Schedule(dayOfWeek = "*", hour = "*", minute = "*/5", second = "0", persistent = false)
	public void every5mitutes() { 
//		log.info("every5minute scheduler method called");
//		
//		ScheduledTaskLog stl = new ScheduledTaskLog();
//		stl.getParamsMap().put("status", "ok");
//		em.persist(stl);
	}
}
