package org.qbix.pm.server.util.collection.predicates;

import java.util.ArrayList;
import java.util.List;

public class PatternPredicate implements Predicate<String>{

	private static final String DEFAULT_SPLIT_PATTERN = "\\s|,|;";
	
	private List<String> tokens;
	
	public PatternPredicate(String pattern) {
		this.tokens = defaultSplit(pattern);
	}
	
	private List<String> splitAndOmitAllEmpty(String string, String token) {
		String[] split = string.split(token);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if (split[i].trim().length() != 0) {
				result.add(split[i]);
			}
		}
		return result;
	}
	
	private List<String> defaultSplit(String string){
		return splitAndOmitAllEmpty(string, DEFAULT_SPLIT_PATTERN);
	}
	
	public boolean isSatisfiedBy(String string) {
		for (String token : tokens) {
			if(satisfiesPattern(string, token)){
				return true;
			}
		}
		return false;
	}

	private boolean satisfiesPattern(String string, String pattern){
		int i = 0;
		int j = 0;
		while (i < pattern.length() && j < string.length()) {
			char filterChar = pattern.charAt(i++);
			char nameChar = string.charAt(j++);
			if (filterChar == '*') {
				char nextChar = 0;
				if(i == pattern.length()){
					j = string.length();
				}else{
					nextChar = pattern.charAt(i);	
				}
				
				while (j != string.length()
						&& nextChar != string.charAt(j)) {
					j++;
				}
			} else if (filterChar == '?') {
			} else {
				if (filterChar != nameChar) {
					break;
				}
			}
		}
		if (i == pattern.length() && j == string.length()) {
			return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return tokens.toString();
	}

}
