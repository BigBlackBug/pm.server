package org.qbix.pm.server.beans;

import javax.ejb.Stateless;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
//TODO remove
public class SimpleBean extends AbstractBean {

	private Logger log = LoggerFactory.getLogger(SimpleBean.class);
	
	public void test(){
		log.info("Hi!!");
	}
	
}
