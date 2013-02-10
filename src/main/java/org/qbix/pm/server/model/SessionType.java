package org.qbix.pm.server.model;

public enum SessionType {

	LOL,

	HON;
	
	public static SessionType getSessionType(String s){
		if(s == null || s.isEmpty()){
			return null;
		}
		s = s.toUpperCase();
		for(SessionType st : SessionType.values()){
			if(st.toString().equals(s)){
				return st;
			}
		}
		return null;
	}

}
