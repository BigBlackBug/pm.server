package org.qbix.pm.server.model;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class UserAccount extends AbstractEntity {

	private static final long serialVersionUID = 2196558275988715559L;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	@JoinColumn(name="lolacc_id")
	private LoLAccount lolAccount;

	private BigDecimal balance = new BigDecimal(0);
	
	private BigDecimal inGameCash = new BigDecimal(0);

	private String nickName;

	private String email;

    /**
     * Пароль храниться ввиде hash md5
     */
	private String password;

    private Boolean accountActivated = false;

	public UserAccount() {
	}

	public LoLAccount getLoLAccount() {
		return lolAccount;
	}

	public void setLoLAccount(LoLAccount lolAccount) {
		this.lolAccount = lolAccount;
	}

	public void setBalance(BigDecimal currency) {
		this.balance = currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public BigDecimal getInGameCash() {
		return inGameCash;
	}
	
	public void setInGameCash(BigDecimal inGameCash) {
		this.inGameCash = inGameCash;
	}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAccountActivated() {
        return accountActivated;
    }

    public void setAccountActivated(Boolean accountActivated) {
        this.accountActivated = accountActivated;
    }
}
