package org.qbix.pm.server.polling;

import org.qbix.pm.server.exceptions.PMPollingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

public class HoNPoller extends AbstractPoller<PollingResult, PollingParams> {
	public static transient final long POLLER_ID = 0L;
	private static final String API_TOKEN = "012WIBLVL6H0SY3B";
	private static Logger log = LoggerFactory.getLogger(HoNPoller.class);
	
	@Override
	protected JsonObject getJson(PollingParams params) throws PMPollingException {
		// TODO Auto-generated method stub
		return null;
	}

}
