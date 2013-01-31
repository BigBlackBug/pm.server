package org.qbix.pm.server.init;

import javax.annotation.PostConstruct;

import javax.ejb.Singleton;
import javax.ejb.Startup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class Initializer {

	private Logger log = LoggerFactory.getLogger(Initializer.class);

	public Initializer() {
	}

	@PostConstruct
	public void init() {
		log.info(Initializer.class.getName() + " initialized");
	}

}
