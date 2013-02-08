package org.qbix.pm.server.polling;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.beans.AbstractBean;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Startup
public class PollingScheduler extends AbstractBean {

	private static Logger log = LoggerFactory.getLogger(PollingScheduler.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;
	
	@Resource
	private SessionContext ctx;
	
	@Traceable
	@Schedule(dayOfWeek = "*", hour = "*", minute = "*/2", second = "0", persistent = false)
	public void poll() {
		log.info("polling started");
	
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Session> criteria = builder.createQuery(Session.class);
		Root<Session> p = criteria.from(Session.class);
		Predicate condition = builder.equal(p.get("status"), SessionStatus.POLLING);
		criteria.where(condition);
		TypedQuery<Session> query = em.createQuery(criteria); 
		List<Session> result = query.getResultList();
		
		for(Session s:result){
			PollingBean bean = (PollingBean) ctx.lookup(PollingBean.class.getSimpleName());
			bean.poll(s);
		}
	}
}
