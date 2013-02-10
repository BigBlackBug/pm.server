package org.qbix.pm.server.util;

import java.util.HashMap;
import java.util.Map;

import org.qbix.pm.server.model.parser.AbstractParser;
import org.qbix.pm.server.model.parser.HoNParser;
import org.qbix.pm.server.model.parser.LoLParser;
import org.qbix.pm.server.polling.AbstractPoller;
import org.qbix.pm.server.polling.HoNPoller;
import org.qbix.pm.server.polling.LoLPoller;
import org.qbix.pm.server.polling.PollingParams;
import org.qbix.pm.server.polling.PollingResult;

public class ServiceUnitHolder {
	private static final Map<Long,Class<?>> STUFF_HOLDER;
	
	static{
		STUFF_HOLDER = new HashMap<Long, Class<?>>();
		STUFF_HOLDER.put(HoNPoller.POLLER_ID, HoNPoller.class);
		STUFF_HOLDER.put(HoNParser.PARSER_ID, HoNParser.class);
		STUFF_HOLDER.put(LoLPoller.POLLER_ID, HoNPoller.class);
		STUFF_HOLDER.put(LoLParser.PARSER_ID, HoNParser.class);
	}
	
	@SuppressWarnings("unchecked")
	public static AbstractPoller<PollingResult, PollingParams> getPoller(long id) {
		Object newInstance = getObject(id);
		return (AbstractPoller<PollingResult, PollingParams>) newInstance;
	}
	
	public static AbstractParser getVCParser(long id) {
		Object newInstance = getObject(id);
		return (AbstractParser) newInstance;
	}
	
	private static Object getObject(long id){
		Class<?> klass = STUFF_HOLDER.get(id);
		Object newInstance = null;
		try {
			newInstance = klass.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newInstance;
	}

}
