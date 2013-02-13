package org.qbix.pm.server.polling;

import java.util.Date;

import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.qbix.pm.server.exceptions.PMParsingException;
import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.VictoryCriteria;
import org.qbix.pm.server.model.parser.AbstractParser;
import org.qbix.pm.server.util.ServiceUnitHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonObject;

public abstract class AbstractPoller<T extends PollingResult, P extends PollingParams> {

	private static Logger log = LoggerFactory.getLogger(AbstractPoller.class);
	
	@SuppressWarnings("unchecked")
	public T poll(P params) throws PMPollingException, PMParsingException{
		Session session = params.getSession();
		VictoryCriteria victoryCriteria = session.getVictoryCriteria();
		Long parserId = victoryCriteria.getParserId();
		AbstractParser parser = ServiceUnitHolder.getVCParser(parserId);

		PollingResult ans = new PollingResult();
		ans.setTimestamp(new Date());
		ans.setSession(params.getSession());
		JsonObject dateJson = null;
		JsonObject dataJson = null;
		try {
			dateJson = getDateJson(params);
		} catch (PMPollingException ex) {
			ans.setReturnCode(ReturnCode.valueOf(ex.getCode()));//TODO may be different
			throw ex;              
		}
		
		boolean gameFinished = parser.isGameFinished(dateJson, session);
		
		ans.setGameFinished(gameFinished);
		if (gameFinished) {
			try {
				dataJson = getDataJson(params);
			} catch (PMPollingException ex) {
				ans.setReturnCode(ReturnCode.valueOf(ex.getCode()));//TODO may be different
				throw ex; 
			}
			ans.setReturnCode(ReturnCode.SUCCESS);
			ans.setJsonParams(dataJson.toString());
			SessionTeam winner = parser.getWinner(dataJson, session);
			ans.setWinnerTeam(winner);
		}
		return (T) ans;
	}

	protected String sendRequest(String url) throws PMPollingException {
		ClientRequest request = new ClientRequest(url);

		request.accept(MediaType.APPLICATION_JSON);

		ClientResponse<String> response = null;
		try {
			response = request.get(String.class);
		} catch (Exception e) {
			log.warn("unable to execute request");
			throw new PMPollingException("unable to execute request",
					ReturnCode.UNKNOWN_ERROR);
		}

		int statusCode = response.getStatus();
		if (statusCode != 200) {
			log.warn("request failed: error_code = " + statusCode);
			throw new PMPollingException("unable to execute request",
					statusCode);
		}
		String json = response.getEntity();
		return json;
	}
	
	protected abstract JsonObject getDataJson(P params) throws PMPollingException;
	
	protected abstract JsonObject getDateJson(P params) throws PMPollingException;
	
}
