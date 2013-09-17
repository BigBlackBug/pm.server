package org.qbix.pm.server.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.validation.ValidationException;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.dto.Notification;
import org.qbix.pm.server.dto.NotificationType;
import org.qbix.pm.server.dto.ParticipantsInfo;
import org.qbix.pm.server.dto.ParticipantsReturnInfo;
import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.SessionInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.util.collection.CollectionUtils;
import org.qbix.pm.server.util.collection.returnfilters.ReturnFilter;

import com.google.gson.Gson;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Traceable
public class SessionFacadeBean extends AbstractBean implements SessionFacade,
		ClientAPI {

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	@EJB
	private ValidationBean validationBean;

	@EJB
	private SessionLifeCycleBean lifecycleBean;

	@EJB
	private NotificationBean notifier;

	@Override
	public Long registerSession(SessionInfo si) throws PMException {
		Session newSession = si.convertToEntity(em);
		newSession = validationBean.validateSessionBeforeRegister(newSession);
		return lifecycleBean.registerSession(newSession);
	}

	@Override
	public Long updateSession(SessionInfo si) throws PMException {
		Session newSession = si.convertToEntity(em);
		lockEntity(Session.class, si.getSessid());
		newSession = validationBean.validateSessionBeforeUpdating(newSession);
		Long updateSession = lifecycleBean.updateSession(newSession);
		Gson gson = new Gson();
		String json = gson.toJson(si);
		notifier.notify(new Notification(
				NotificationType.SESSION_PARAMETERS_CHANGED,
				getAccountIds(newSession), json));
		return updateSession;
	}

	@Override
	public Long updateLoLParticipants(ParticipantsInfo info) throws PMException {
		Session session = lockEntity(Session.class, info.getSessionId());
		info = validationBean.validateParticipantsInfo(info);
		ParticipantsReturnInfo returnInfo = lifecycleBean.updateLoLParicipants(info);
		notifier.notify(new Notification(NotificationType.UPDATE_WITH_GAMEDTO,
				getAccountIds(session), new Gson().toJson(returnInfo,
						ParticipantsReturnInfo.class)));
		
		return session.getId();
	}

	@Override
	public void playerDisconnected(UserJoinInfo uji) throws PMException {
		lockEntity(Session.class, uji.getSessid());
		uji = validationBean.validateUserJoinInfoBeforeDisconnecting(uji);
		lifecycleBean.playerDisconnected(uji);
		Session session = em.find(Session.class, uji.getSessid());
		notifier.notify(new Notification(NotificationType.PLAYER_DISCONNECTED,
				getAccountIds(session), "account_id", uji.getAccountid()));
	}

	@Override
	public void confirmParticipation(UserJoinInfo uji) throws PMException {
		lockEntity(Session.class, uji.getSessid());
		uji = validationBean.validateUserJoinInfoBeforeAdding(uji);
		lifecycleBean.confirmParticipation(uji);
		Session session = em.find(Session.class, uji.getSessid());
		notifier.notify(new Notification(
				NotificationType.PLAYER_CONFIRMED_STAKE,
				getAccountIds(session), "account_id", uji.getAccountid()));
	}

	@Override
	public void cancelParticipation(UserJoinInfo uji) throws PMException {
		lockEntity(Session.class, uji.getSessid());
		uji = validationBean.validateUserJoinInfoBeforeDisconnecting(uji);
		lifecycleBean.cancelParticipation(uji);

		Session session = em.find(Session.class, uji.getSessid());
		notifier.notify(new Notification(
				NotificationType.PLAYER_CANCELLED_STAKE,
				getAccountIds(session), "account_id", uji.getAccountid()));
	}

	@Override
	public void startSession(SessionInfo si) throws PMException {
		Session session = si.convertToEntity(em);
		lockEntity(Session.class, si.getSessid());
		session = validationBean.validateSessionBeforeStart(session);
		lifecycleBean.startSession(session);
		notifier.notify(new Notification(NotificationType.SESSION_STARTED,
				getAccountIds(session)));
	}

	@Override
	public void resolveResult(ResultInfo ri) throws PMException {
		lockEntity(Session.class, ri.getSessid());
		ri = validationBean.validateResult(ri);
		lifecycleBean.resolveResultAndCloseSession(ri);
	}

	private <T> T lockEntity(Class<T> entityClass, Object id) {
		if (id == null) {
			throw new ValidationException("Entity id cant be null");
		}

		T entity = em.find(entityClass, id, LockModeType.PESSIMISTIC_WRITE);

		if (entity == null) {
			throw new ValidationException("No entity with id = " + id);
		}
		return entity;
	}

	private List<Long> getAccountIds(Session session) {
		return CollectionUtils.filterEntities(session.getPlayers(),
			new ReturnFilter<PlayerEntry, Long>() {
				@Override
				public Long returns(PlayerEntry entity) {
					return entity.getAccount().getId();
				}
			}).toList();
	}

}
