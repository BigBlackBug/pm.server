package org.qbix.pm.server.beans;

import javax.interceptor.Interceptors;

import org.qbix.pm.server.intercept.TraceInterceptor;

@Interceptors({TraceInterceptor.class})
public abstract class AbstractBean {

}
