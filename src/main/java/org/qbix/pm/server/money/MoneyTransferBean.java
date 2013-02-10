package org.qbix.pm.server.money;

import java.math.BigDecimal;
import java.util.Date;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.beans.AbstractBean;
import org.qbix.pm.server.exceptions.PMTransferMoneyException;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.UserAccount;
import org.qbix.pm.server.money.MoneyTransferLogEntry.MoneyTransferAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Traceable
@TransactionAttribute(TransactionAttributeType.MANDATORY)
public class MoneyTransferBean extends AbstractBean {

	private static final Logger log = LoggerFactory
			.getLogger(MoneyTransferBean.class);

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	public void transfer(SimpleMoneyTransferInfo mti)
			throws PMTransferMoneyException {

		log.info(String.format("transfering session(id%d) result's money",
				mti.getSessionId()));

		// TODO need validation here ??
		Session sess = em.find(Session.class, mti.getSessionId());

		for (Long acc : mti.getTransferDetails().keySet()) {
			UserAccount ua = em.find(UserAccount.class, acc);
			addToUserAcc(ua, mti.getTransferDetails().get(acc),
					MoneyTransferAction.SESSION_RESULT_RESOLVING, sess);
		}
	}

	/**
	 * @param account
	 *            should be managed!
	 * @param sess
	 *            should be managed!
	 */
	public void userSessionParticipation(Session sess, UserAccount userAcc,
			BigDecimal stake) {
		addToUserAcc(userAcc, stake, MoneyTransferAction.SESSION_PLAYER_PARTICIPATION,
				sess);
	}

	/**
	 * @param account
	 *            should be managed!
	 * @param sess
	 *            session(should be managed!), can be null if transfer operation
	 *            is not in session context
	 */
	private void addToUserAcc(UserAccount account, BigDecimal money,
			MoneyTransferAction action, Session sess) {

		account.setCurrency(account.getCurrency().add(money));

		MoneyTransferLogEntry mtle = new MoneyTransferLogEntry();
		mtle.setTargetAccount(account);
		mtle.setCurrency(money);
		mtle.setAction(action);
		mtle.setTimestamp(new Date());
		mtle.setSession(sess);
		em.persist(mtle);
	}
}
