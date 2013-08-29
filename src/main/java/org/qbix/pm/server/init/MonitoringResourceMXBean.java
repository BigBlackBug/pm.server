package org.qbix.pm.server.init;

import javax.management.MXBean;

@MXBean
public interface MonitoringResourceMXBean {

	public void logInfo(String message);
	
	public String getCurrentYMBalance(int startRec, int recCount);
}
