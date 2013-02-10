package org.qbix.pm.server.model;

public enum SessionTeam {
	TEAM_0(0), 
	
	TEAM_1(1);
	
	private int code;

	private SessionTeam(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
	public static SessionTeam valueOf(int code) throws IllegalArgumentException{
		if(code == TEAM_0.code){
			return SessionTeam.TEAM_0;
		}else{
			return SessionTeam.TEAM_1;
		}
	}
	
}