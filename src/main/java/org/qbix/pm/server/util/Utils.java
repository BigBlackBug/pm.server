package org.qbix.pm.server.util;

import java.math.BigDecimal;

public class Utils {

	public static boolean gt0(BigDecimal bd){
		return bd.compareTo(new BigDecimal(0)) == 1;
	}
	
	public static boolean gte0(BigDecimal bd){
		return bd.compareTo(new BigDecimal(0)) >= 0;
	}
	
	public static boolean eq0(BigDecimal bd){
		return bd.compareTo(new BigDecimal(0)) == 0;
	}
	
	public static boolean lt0(BigDecimal bd){
		return bd.compareTo(new BigDecimal(0)) == -1;
	}
	
	public static boolean lte0(BigDecimal bd){
		return bd.compareTo(new BigDecimal(0)) <= 0;
	}
	
}
