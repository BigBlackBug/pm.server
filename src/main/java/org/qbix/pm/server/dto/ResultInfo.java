package org.qbix.pm.server.dto;

import java.io.Serializable;
import java.util.Map;

public class ResultInfo implements Serializable {

	private static final long serialVersionUID = -6725338544884993502L;

	private Long gameID;
	
	/** 0/1 */ 
	private int winner;
	
	private Map<String, Object> params;
	
	public ResultInfo() {
	}
	
	public void setGameID(Long sessid) {
		this.gameID = sessid;
	}
	
	public Long getGameID() {
		return gameID;
	}
	
	public void setWinner(int winner) {
		this.winner = winner;
	}
	
	public int getWinner() {
		return winner;
	}
	
	
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	
	public Map<String, Object> getParams() {
		return params;
	}
	
}
