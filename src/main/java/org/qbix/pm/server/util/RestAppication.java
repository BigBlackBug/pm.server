package org.qbix.pm.server.util;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.exceptions.PMValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("rs")
public class RestAppication extends Application {

	@Provider
	public static class PMExceptionMapper implements
			ExceptionMapper<PMException> {

		private Logger log = LoggerFactory.getLogger(PMExceptionMapper.class);

		@Override
		public Response toResponse(PMException exception) {

			if (exception instanceof PMValidationException) {
				/**
				 * this is user invalid user request exceptions - no need to log
				 * on server
				 */
				log.debug("BAD REQUEST: " + exception.getMessage());
				return Response.status(Status.BAD_REQUEST).build();
			}

			log.warn(exception.getMessage(), exception);
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}

	}
}
