package org.qbix.pm.server.init;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.model.Ent;
import org.qbix.pm.server.mongo.MongoBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class Initializer {
	
	private Logger log = LoggerFactory.getLogger(Initializer.class);
	
	@PersistenceContext(unitName="pm")
	private EntityManager em;
	
	@EJB
	private MongoBean mb;
	
	public Initializer() {
	}
	
	@PostConstruct
	public void init(){
		//TODO remove 
		Ent e  = new Ent();
		e.setName("date: " + new Date());
		em.persist(e);
		em.flush();
		em.refresh(e);
		
		mb.saveOrUpdate(e);
		
		log.info(Initializer.class.getName() + " initialized");
	}
	
}
