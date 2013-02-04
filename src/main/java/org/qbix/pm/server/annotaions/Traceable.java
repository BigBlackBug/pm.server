package org.qbix.pm.server.annotaions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Traceable {
	
	LOG_LEVEL LOG_LEVEL() default LOG_LEVEL.DEBUG;
	 
	 public static enum LOG_LEVEL {
		 TRACE, DEBUG, INFO, WARN, ERROR;
	 }
}
