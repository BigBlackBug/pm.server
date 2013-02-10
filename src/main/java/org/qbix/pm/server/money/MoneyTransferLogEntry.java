package org.qbix.pm.server.money;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.qbix.pm.server.model.AbstractEntity;
import org.qbix.pm.server.model.Session;
import org.qbix.pm.server.model.UserAccount;

@Entity
@Table(name = "money_transfer_logs")
public class MoneyTransferLogEntry extends AbstractEntity {

	private static final long serialVersionUID = 4149044336184589883L;

	public static enum MoneyTransferAction {
		SESSION_PLAYER_PARTICIPATION, SESSION_RESULT_RESOLVING, BALANCE_RECHARGE
	}

	@ManyToOne
	@JoinColumn(name = "target_account_id")
	@NotNull
	private UserAccount targetAccount;

	@Enumerated(EnumType.STRING)
	@NotNull
	private MoneyTransferAction action;

	@NotNull
	private BigDecimal currency;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date timestamp;

	/** can be null */
	@ManyToOne
	@JoinColumn(name = "session_id")
	private Session session;

	public MoneyTransferLogEntry() {
	}

	public void setTargetAccount(UserAccount targetAccount) {
		this.targetAccount = targetAccount;
	}

	public UserAccount getTargetAccount() {
		return targetAccount;
	}

	public void setAction(MoneyTransferAction action) {
		this.action = action;
	}

	public MoneyTransferAction getAction() {
		return action;
	}

	public void setCurrency(BigDecimal currency) {
		this.currency = currency;
	}

	public BigDecimal getCurrency() {
		return currency;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public Session getSession() {
		return session;
	}
}
