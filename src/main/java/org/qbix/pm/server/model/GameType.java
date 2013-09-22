package org.qbix.pm.server.model;

public enum GameType {

	LOL,

	HON;
	
	public static GameType getSessionType(String s){
		if(s == null || s.isEmpty()){
			return null;
		}
		s = s.toUpperCase();
		for(GameType st : GameType.values()){
			if(st.toString().equals(s)){
				return st;
			}
		}
		return null;
	}

}
