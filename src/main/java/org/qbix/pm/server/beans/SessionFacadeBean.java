package org.qbix.pm.server.beans;

import java.math.BigDecimal;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.SessionInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Traceable
public class SessionFacadeBean extends AbstractBean implements SessionFacade,
		ClientAPI {

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
		Session newSession = si.convertToEntity(em);
		newSession = validationBean.validateSessionBeforeRegister(newSession);
		return lifecycleBean.registerSession(newSession);
	}

	@Override
	public void confirmParticipation(UserJoinInfo uji) throws PMException {
		uji = validationBean.validateUserJoinInfo(uji);
		lifecycleBean.confirmParticipation(uji);
		log.info(String.format(
				"user(id%d) confirmed participation is session(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	@Override
	public void cancelParticipation(UserJoinInfo uji) throws PMException {
		log.info("in cancelParticipation");
		for (PlayerEntry pe : em.getReference(Session.class, uji.getSessid())
				.getPlayers()) {
			if (pe.getAccount().getId().equals(uji.getAccountid())) {
				pe.setStake(new BigDecimal(-1));
				break;
			}
		}
	}

	@Override
	public void startSession(SessionInfo si) throws PMException {
		Session sess = si.convertToEntity(em);
		sess = validationBean.validateSessionBeforeStart(sess);
		lifecycleBean.startSession(sess);
	}

	@Override
	public void resolveResult(ResultInfo ri) throws PMException {
		ri = validationBean.validateResult(ri);
		lifecycleBean.resolveResultAndCloseSession(ri);
	}

}
