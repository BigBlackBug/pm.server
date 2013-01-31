package org.qbix.pm.server.beans;

import javax.ejb.Stateless;
import org.qbix.pm.server.annotaions.Traceble;
import org.qbix.pm.server.dto.SessionInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Traceble
public class SessionFacadeBean extends AbstractBean implements SessionFacade,
		ClientAPI {

	protected Logger log = LoggerFactory.getLogger(SessionFacadeBean.class);

	@Override
	public Long registerSession(SessionInfo si) throws PMException {
		log.info("in registerSession.");
		log.info(si.toString());
		return 1L;
	}

	@Override
	public void confirmParticipation(UserJoinInfo uji) throws PMException {
		log.info("in confirmParticipation");
	}

	@Override
	public void startSession(Long sessId) throws PMException {
		log.info("in startSessin");
	}

}
