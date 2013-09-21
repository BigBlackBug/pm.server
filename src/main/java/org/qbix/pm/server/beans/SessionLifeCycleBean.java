package org.qbix.pm.server.beans;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.TypedQuery;

import org.qbix.pm.server.dto.ParticipantsInfo;
import org.qbix.pm.server.dto.ParticipantsReturnInfo;
import org.qbix.pm.server.dto.ParticipantsReturnInfo.Entry;
import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMLifecycleException;
import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.SessionStatus;
import org.qbix.pm.server.model.SessionTeam;
import org.qbix.pm.server.model.UserAccount;
import org.qbix.pm.server.model.VictoryCriteria;
import org.qbix.pm.server.money.MoneyTransferBean;
import org.qbix.pm.server.money.SimpleMoneyTransferInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class SessionLifeCycleBean extends AbstractBean {

	private static Logger log = LoggerFactory
			.getLogger(SessionLifeCycleBean.class);

	@EJB
	private SessionFacade sessionFacade;

	@EJB
	private MoneyTransferBean moneyTransfer;

	public Long registerSession(Session newSession) {
		newSession.setStatus(SessionStatus.ACCEPTING_PLAYERS);
		em.persist(newSession);
		em.flush();
		em.refresh(newSession);

		log.info(String.format("session(id%d) registered", newSession.getId()));
		return newSession.getId();
	}

	public Long updateSession(Session session) {
		Session managedSession = em.find(Session.class, session.getId());

		BigDecimal stake = session.getStake();
		if (stake != null) {
			managedSession.setStake(stake);
		}

		VictoryCriteria vc = session.getVictoryCriteria();
		if (vc != null) {
			managedSession.setVictoryCriteria(vc);
		}
		
		log.info(String.format("session(id%d) updated.", session.getId()));

		return session.getId();
	}

	public ParticipantsReturnInfo updateLoLParticipants(ParticipantsInfo si) {
		Session session = em.find(Session.class, si.getSessionId());

		List<Long> teamOne = si.getTeamOne();
		List<Long> teamTwo = si.getTeamTwo();

		Set<PlayerEntry> players = session.getPlayers();

		Set<PlayerEntry> toRemove = new HashSet<>();
		for (PlayerEntry pe : players) {
			Long loAaccountId = pe.getAccount().getLoLAccount().getAccountID();
			if (teamOne.contains(loAaccountId)) {
				pe.setTeam(SessionTeam.TEAM_0);
				teamOne.remove(loAaccountId);
			} else if (teamTwo.contains(loAaccountId)) {
				pe.setTeam(SessionTeam.TEAM_1);
				teamTwo.remove(loAaccountId);
			} else {
				toRemove.add(pe);
			}
		}

		players.removeAll(toRemove);

		addNewEntries(session, players, teamOne, SessionTeam.TEAM_0);
		addNewEntries(session, players, teamTwo, SessionTeam.TEAM_1);

		log.info(String.format("session(id%d) updated participants.",
				session.getId()));

		return getParticipantsReturnInfo(session, players);
	}

	private ParticipantsReturnInfo getParticipantsReturnInfo(Session session,
			Set<PlayerEntry> players) {
		ParticipantsReturnInfo info = new ParticipantsReturnInfo();
		info.setSessionId(session.getId());
		for (PlayerEntry playerEntry : players) {
			SessionTeam team = playerEntry.getTeam();
			UserAccount account = playerEntry.getAccount();
			Long lolAccountID = account.getLoLAccount().getAccountID();
			Long pmAccountID = account.getId();
			String nickName = account.getNickName();
			if (team == SessionTeam.TEAM_0) {
				info.putToTeamOne(lolAccountID,
						new Entry(pmAccountID, nickName));
			} else {
				info.putToTeamTwo(lolAccountID,
						new Entry(pmAccountID, nickName));
			}
		}
		return info;
	}

	private void addNewEntries(Session session,
			Set<PlayerEntry> managedPlayers, List<Long> lolAccountIDs,
			SessionTeam team) {
		for (Long id : lolAccountIDs) {
			PlayerEntry entry = new PlayerEntry();
			entry.setAccount(getUserAccountWithLolAccountID(id));
			entry.setSession(session);
			entry.setStake(new BigDecimal(-1));
			entry.setTeam(team);
			managedPlayers.add(entry);
		}
	}

	private UserAccount getUserAccountWithLolAccountID(Long lolAccountId) {
		String q = "SELECT u FROM" + UserAccount.class.getName()
				+ " u JOIN u.lolAccount AS lolacc where lolacc.accountID = :id";

		TypedQuery<UserAccount> query = em.createQuery(q, UserAccount.class)
				.setParameter("id", lolAccountId);

		UserAccount acc = query.getSingleResult();
		return acc;
	}

	public void playerDisconnected(UserJoinInfo uji) {
		Session sess = em.find(Session.class, uji.getSessid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());

		Iterator<PlayerEntry> it = sess.getPlayers().iterator();
		while (it.hasNext()) {
			PlayerEntry next = it.next();
			if (next.getAccount().equals(acc)) {
				it.remove();
				break;
			}
		}
		log.info(String.format("user(id%d) disconnected from session(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	public void confirmParticipation(UserJoinInfo uji) {
		Session sess = em.find(Session.class, uji.getSessid());
		UserAccount acc = em.find(UserAccount.class, uji.getAccountid());

		for (PlayerEntry pe : sess.getPlayers()) {
			if (pe.getAccount().equals(acc)) {
				pe.setStake(sess.getStake());
				break;
			}
		}

		if (checkIsSessionIsReadyToStart(sess)) {
			sess.setStatus(SessionStatus.READY_TO_START);
		}

		log.info(String.format(
				"user(id%d) confirmed participation is session(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	public void cancelParticipation(UserJoinInfo uji) {
		for (PlayerEntry pe : em.getReference(Session.class, uji.getSessid())
				.getPlayers()) {
			if (pe.getAccount().getId().equals(uji.getAccountid())) {
				pe.setStake(new BigDecimal(-1));
				break;
			}
		}

		log.info(String.format(
				"user(id%d) cancelled participation is session(id%d)",
				uji.getAccountid(), uji.getSessid()));
	}

	private boolean checkIsSessionIsReadyToStart(Session sess) {
		// check status required ???
		for (PlayerEntry pe : sess.getPlayers()) {
			if (pe.getStake().doubleValue() < 0) {
				return false;
			}
		}

		return true;
	}

	public void startSession(Session sess) throws PMLifecycleException {
		sess = em.find(Session.class, sess.getId());
		sess.setStatus(SessionStatus.SESSION_COMMITED);
		log.info(String.format("session(id%d) started", sess.getId()));
	}

	public void resolveResultAndCloseSession(ResultInfo ri)
			throws PMLifecycleException {
		SimpleMoneyTransferInfo mti = getMoneyTransferInfo(ri);
		moneyTransfer.transfer(mti);

		em.find(Session.class, ri.getSessid()).setStatus(SessionStatus.CLOSED);
		log.info("session's(id%d) result resolved, session closed");
	}

	private SimpleMoneyTransferInfo getMoneyTransferInfo(ResultInfo ri) {
		log.info(String.format("analyzing session(id%s) result", ri.getSessid()));

		Session sess = em.find(Session.class, ri.getSessid());
		SessionTeam winnerTeam = SessionTeam.valueOf(ri.getWinner());
		List<Long> winners = new ArrayList<Long>();
		BigDecimal winnersMoney = new BigDecimal(0);
		for (PlayerEntry pe : sess.getPlayers()) {
			// fucking BigDecimal!
			winnersMoney = new BigDecimal(winnersMoney.doubleValue()
					+ pe.getStake().doubleValue());

			if (pe.getTeam().getCode() == winnerTeam.getCode()) {
				winners.add(pe.getAccount().getId());
			}
		}

		BigDecimal moneyFor1Winner = winnersMoney.divide(
				new BigDecimal(winners.size()), 2);

		SimpleMoneyTransferInfo mti = new SimpleMoneyTransferInfo(sess.getId());

		for (Long winnerAccId : winners) {
			mti.getTransferDetails().put(winnerAccId, moneyFor1Winner);
		}

		return mti;
	}

}
