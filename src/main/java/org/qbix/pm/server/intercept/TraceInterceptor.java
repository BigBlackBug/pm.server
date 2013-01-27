package org.qbix.pm.server.intercept;

import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptor;
import javax.interceptor.InvocationContext;

import org.qbix.pm.server.annotaions.Traceble;
import org.qbix.pm.server.annotaions.Traceble.LOG_LEVEL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Interceptor
public class TraceInterceptor {

	private Logger log = LoggerFactory.getLogger(TraceInterceptor.class);

	@AroundInvoke
	public Object intercept(InvocationContext aCtx) throws Exception {

		LOG_LEVEL lglvl = null;

		if (aCtx.getTarget().getClass().isAnnotationPresent(Traceble.class)) {
			lglvl = aCtx.getTarget().getClass().getAnnotation(Traceble.class)
					.LOG_LEVEL();
		}
		
		if (aCtx.getMethod().isAnnotationPresent(Traceble.class)) {
			lglvl = aCtx.getMethod().getAnnotation(Traceble.class).LOG_LEVEL();
		}
		
		if (lglvl == null) {
			return aCtx.proceed();
		}

		long before = System.currentTimeMillis();
		try {
			return aCtx.proceed();
		} finally {
			long diff = System.currentTimeMillis() - before;
			String mess = aCtx.getTarget().getClass().getSimpleName() + "."
					+ aCtx.getMethod().getName() + " took " + diff + "ms";

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