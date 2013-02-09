package org.qbix.pm.server.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Traceable {
	
	LogLevel logLevel() default LogLevel.DEBUG;
	 
	 public static enum LogLevel {
		 TRACE, DEBUG, INFO, WARN, ERROR;
	 }
}
