package org.qbix.pm.server.mongo;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Startup;
import javax.ejb.Stateless;

import org.qbix.pm.server.annotaions.Traceble;
import org.qbix.pm.server.beans.AbstractBean;
import org.qbix.pm.server.util.SpringConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoOperations;

@Stateless
@Startup
@Traceble
public class MongoBean extends AbstractBean {

	private Logger log = LoggerFactory.getLogger(MongoBean.class);

	protected MongoOperations mOps;

	@PostConstruct
	public void init() {
		mOps = SpringConfig.getBean("mongoTemplate");
		log.info(MongoBean.class.getSimpleName() + " initialized");
	}

	public <T> void insert(T obj) {
		mOps.insert(obj);
	}

	public <T> void saveOrUpdate(T obj) {
		mOps.save(obj);
	}

	public <T> List<T> getAll(Class<T> clazz) {
		return mOps.findAll(clazz);
	}

	// Добавить сюда необходимые методы
}
