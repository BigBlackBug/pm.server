package org.qbix.pm.server.util;

import org.qbix.pm.server.model.SessionType;

public class ParsingUtils {

	public static SessionType getSessTypeByString(String s){
		if(s == null || s.isEmpty()){
			return null;
		}
		s = s.toUpperCase();
		for(SessionType st : SessionType.values()){
			if(st.toString().equals(st)){
				return st;
			}
		}
		return null;
	}
	
}
