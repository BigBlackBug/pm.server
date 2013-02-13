package org.qbix.pm.server.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StringUtils {
	private static final String DELIMITER = " ";
	private static final String SEPARATOR = "";
	private static final String FILLER = " ";
	private static final String PREFIX = "";

	public static List<String> splitAndOmitAllEmpty(String string, String token) {
		String[] split = string.split(token);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			if (split[i].trim().length() != 0) {
				result.add(split[i]);
			}
		}
		return result;
	}

	public static String join(List<String> args, String token) {
		StringBuilder sb = new StringBuilder();
		for (String arg : args) {
			sb.append(arg).append(token);
		}
		if (sb.length() != 0)
			sb.delete(sb.length() - token.length(), sb.length());
		return sb.toString();
	}

	public static String reduceSpaces(String source) {
		return source.trim().replaceAll(" +", " ");
	}

	public static String getWordSplitter(String word) {
		return "\\b" + word + "\\b";
	}

	public static boolean containsWord(String string, String word) {
		return string.matches("^.*" + getWordSplitter(word) + ".*$");
	}

	public static boolean isQuotationMark(char c){
		return c == '\'' || c == '\"';
	}

	public static String getPlural(long value, String string) {
		if (value != 1 && value != -1) {
			return value + " " + string + "s";
		} else {
			return value + " " + string;
		}
	}

	public static List<String> trimAll(List<String> list) {
		List<String> res = new ArrayList<String>();
		for (String string : list) {
			res.add(string.trim());
		}
		return res;
	}

	public static List<String> splitAndOmitTrailing(String string, String token) {
		String[] split = string.split(token);
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < split.length; i++) {
			result.add(split[i]);
		}
		if (result.get(0).trim().length() == 0)
			result.remove(0);
		if (result.size() != 0) {
			if (result.get(result.size() - 1).trim().length() == 0)
				result.remove(result.size() - 1);
		}
		return result;
	}

	public static <T> Set<T> filterEntities(List<T> source, String pattern, Processor<T> preprocessor) {
		
		List<String> tokens = StringUtils.splitAndOmitAllEmpty(pattern, "\\s|,|;");
		Set<T> filteredTables = new HashSet<T>();
		
		for (T entity : source) {
			for (String token : tokens) {
				T processedEntity = preprocessor.process(entity);
				if (satisfiesCriteria(processedEntity.toString(), token)) {
					filteredTables.add(entity);
				}
			}
		}
		return filteredTables;
	}
	
	public static boolean satisfiesCriteria(String string, String criteria){
		int i = 0;
		int j = 0;
		while (i < criteria.length() && j < string.length()) {
			char filterChar = criteria.charAt(i++);
			char nameChar = string.charAt(j++);
			if (filterChar == '*') {
				char nextChar = 0;
				if(i == criteria.length()){
					j = string.length();
				}else{
					nextChar = criteria.charAt(i);	
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
		if (i == criteria.length() && j == string.length()) {
			return true;
		}
		return false;
	}
	
	public static int countOccurences(String string, String token) {
		int count = 0;
		int idx = 0;
		while ((idx = string.indexOf(token, idx)) != -1) {
			count++;
			idx += token.length();
		}
		return count;
	}

	public static boolean isNumber(String arg) {
		try {
			Integer.parseInt(arg);
		} catch (NumberFormatException nex) {
			return false;
		}
		return true;
	}

	private static int findLongestInColumn(List<List<String>> source, int column) {
		int longest = 0;
		for (List<String> line : source) {
			int current = line.get(column).length();
			if (current > longest)
				longest = current;
		}
		return longest;
	}

	public static List<String> format(List<List<String>> source,String separator) {
		ArrayList<String> result = new ArrayList<String>();
		if(!source.isEmpty()){
			int lineLength = source.get(0).size();
			for (int i = 0; i < source.size(); i++) {
				result.add(PREFIX);
			}
						
			for (int col = 0; col < lineLength; col++) {
				int longest = findLongestInColumn(source, col);
	
				for (int j = 0; j < source.size(); j++) {
					List<String> line = source.get(j);
					result.set(j, result.get(j) 
							+ toLength(line.get(col), longest, false)
							+ DELIMITER 
							+ separator
							+ DELIMITER);
				}
			}
		}
		return result;
	}
	
	public static List<String> format(List<List<String>> source){
		return format(source, SEPARATOR);
	}

	public static String toLength(String string, int longest, boolean preceding) {
		int length = string.length();
		String filler = "";
		if (length < longest) {
			for (int i = 0; i < longest - length; i++) {
				filler += FILLER;
			}
		}
		if (preceding) {
			return filler + string;
		} else {
			return string + filler;
		}
	}

	public static String decorate(String description, String value) {
		return description + ": " + quote(value);
	}

	public static String quote(String string) {
		return "'" + string + "'";
	}

	public static boolean isEnclosedInQuotes(String string) {
		return string.matches("'([^']*)'|\"([^\"]*)\"");
	}
	
	public static int digitCount(int value) {
		int k = 10;
		int i = 1;
		while ((value /= k) > 0) {
			i++;
		}
		return i;
	}

//	public static String removeQuotes(String arg) {
//		return arg.replace("['\"]","");
//	}

}
