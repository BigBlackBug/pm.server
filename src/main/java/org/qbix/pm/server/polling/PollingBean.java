package org.qbix.pm.server.polling;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.beans.MongoBean;
import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.util.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class PollingBean {
	private static final Logger log = LoggerFactory.getLogger(MongoBean.class);
	
	@PersistenceContext(unitName = "pm")
	private EntityManager em;
	
	public void poll(Session session){
		PollingParams pollingParams = session.getPollingParams();
		AbstractPoller<PollingResult, PollingParams> p = Cache.getPoller(pollingParams.getPollerId());
		PollingResult result = null;
		try {
			result = p.poll(pollingParams);
		} catch (PMPollingException e) {
			log.warn(e.getMessage());		
		}
		
		PollingLogEntry log=new PollingLogEntry();
		log.setJsonParams(result.getJsonParams());
		log.setReturnCode(result.getReturnCode());
		log.setTimestamp(result.getTimestamp());
		em.persist(log);
		
		if(result.isGameFinished()){
			em.persist(result);
			resolveResult(result);
		}
	}

	private void resolveResult(PollingResult result) {
		// TODO write resolver
	}
}
