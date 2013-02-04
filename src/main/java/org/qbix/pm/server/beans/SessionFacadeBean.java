package org.qbix.pm.server.beans;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.dto.SessionInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.polling.PollingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Traceable
public class SessionFacadeBean extends AbstractBean implements SessionFacade {

	private static Logger log = LoggerFactory
			.getLogger(SessionFacadeBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	@EJB
	private ValidationBean validationBean;

	@EJB
	private SessionLifeCycleBean lifecycleBean;

	@Override
	public Long registerSession(SessionInfo si) throws PMException {
		Session newSession = si.convertToEntity();
		newSession = validationBean.validateSessionBeforeRegister(newSession);
		return lifecycleBean.registerSession(newSession);
	}

	@Override
	public void startPlayersConfirmation(SessionInfo si) throws PMException {
		Session sess = si.convertToEntity();
		sess = validationBean.validateSessionBeforeConfStart(sess);
		lifecycleBean.startSessionConfirmation(sess);
	}

	@Override
	public void confirmParticipation(UserJoinInfo uji) throws PMException {
		uji = validationBean.validateUserJoinInfo(uji);
		lifecycleBean.confirmParticipation(uji);
	}

	@Override
	public void cancelParticipation(UserJoinInfo uji) throws PMException {
		log.info("in cancelParticipation");
	}

	@Override
	public void startSession(SessionInfo si) throws PMException {
		Session sess = si.convertToEntity();
		sess =  validationBean.validateSessionBeforeStart(sess);
		lifecycleBean.startSession(sess);
	}

	@Override
	public void resolveResult(PollingResult pr) throws PMLifecycleException {
		// TODO Auto-generated method stub
	}
	
}
