package org.qbix.pm.server.init;

import java.lang.management.ManagementFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.qbix.pm.server.money.YMFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
@Startup
public class MonitoringResource implements MonitoringResourceMXBean {

	private Logger log = LoggerFactory.getLogger(MonitoringResource.class);

	protected MBeanServer platformMBeanServer;
	protected ObjectName objectName = null;

	@PostConstruct
	public void registerInJMX() {
		try {
			objectName = new ObjectName("PMMonitoring:type="
					+ this.getClass().getName());
			platformMBeanServer = ManagementFactory.getPlatformMBeanServer();
			platformMBeanServer.registerMBean(this, objectName);
		} catch (Exception e) {
			throw new IllegalStateException(
					"Problem during registration of Monitoring into JMX:" + e);
		}
	}

	@PreDestroy
	public void unregisterFromJMX() {
		try {
			platformMBeanServer.unregisterMBean(objectName);
		} catch (Exception e) {
			throw new IllegalStateException(
					"Problem during unregistration of Monitoring into JMX:" + e);
		}
	}

	@Override
	public void logInfo(String message) {
		log.info(message);
	}

	@Override
	public String getCurrentYMBalance(int startRec, int recCount) {
		try {
			return new YMFacade().getPaymentHistoryAsString(startRec, recCount);
		} catch (Exception e) {
			log.error("mxbean err", e);
			return "error";
		}
	}

}
