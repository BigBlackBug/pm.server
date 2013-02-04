package org.qbix.pm.server.polling;

import java.util.Collections;
import java.util.Map;

import org.qbix.pm.server.exceptions.PMPollingException;
import org.qbix.pm.server.model.Team;
import org.qbix.pm.server.model.VictoryCriteria;
import org.qbix.pm.server.model.parser.AbstractParser;
import org.qbix.pm.server.model.parser.HonParser;


public class HoNPoller extends AbstractPoller<DefaultPollingResult, PollingParams>{

	@Override
	public DefaultPollingResult poll(PollingParams params) throws PMPollingException {
		VictoryCriteria victoryCriteria = params.getSession().getVictoryCriteria();
		Long parserId = victoryCriteria.getParserId();
		AbstractParser parser = new HonParser();//getById
		
		Map<String, String> result = getResult();
		
		DefaultPollingResult ans = new DefaultPollingResult();
		if(result.isEmpty()){
			ans.setGameFinished(false);
		}else{
			ans.setGameFinished(true);
			Team winner = parser.getWinner(result, victoryCriteria);
			ans.setWinnerTeam(winner);
		}
		return ans;
	}
	
	private Map<String,String> getResult(){
		return Collections.emptyMap();
	}

}
