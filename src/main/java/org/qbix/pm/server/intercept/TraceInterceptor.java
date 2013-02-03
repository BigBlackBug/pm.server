package org.qbix.pm.server.intercept;

import java.lang.reflect.Method;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.annotaions.Traceable.LOG_LEVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
public class TraceInterceptor {

	private Logger log = LoggerFactory.getLogger(TraceInterceptor.class);

	@AroundInvoke
	public Object intercept(InvocationContext aCtx) throws Exception {

		LOG_LEVEL lglvl = null;
		Class<?> clazz = aCtx.getTarget().getClass();
		Traceable annotation = clazz.getAnnotation(Traceable.class);
		if (annotation != null) {
			lglvl = annotation.LOG_LEVEL();
		}
		
		Method method = aCtx.getMethod();
		annotation = method.getAnnotation(Traceable.class);
		if (annotation != null) {
			lglvl = annotation.LOG_LEVEL();
		}
		
		if (lglvl == null) {
			return aCtx.proceed();
		}

		long before = System.currentTimeMillis();
		try {
			return aCtx.proceed();
		} finally {
			long diff = System.currentTimeMillis() - before;
			String mess = clazz.getSimpleName() + "."
					+ method.getName() + " took " + diff + "ms";

			switch (lglvl) {
			case TRACE:
				log.trace(mess);
				break;
			case DEBUG:
				log.debug(mess);
				break;
			case INFO: 
				log.info(mess);
				break;
			case WARN:
				log.warn(mess);
				break;
			case ERROR: 
				log.error(mess);
				break;
			}
		}
	}
}