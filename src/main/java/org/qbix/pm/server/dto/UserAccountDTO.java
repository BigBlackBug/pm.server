package org.qbix.pm.server.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class UserAccountDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long ID;
	
	private String nickName;

	private String email;

    private Boolean accountActivated;
    
    private String password;
    
    /** read only field */
    private BigDecimal balance;
    
    public UserAccountDTO() {
	}

    public Long getID() {
		return ID;
	}
    
    public void setID(Long iD) {
		ID = iD;
	}
    
	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getAccountActivated() {
		return accountActivated;
	}

	public void setAccountActivated(Boolean accountActivated) {
		this.accountActivated = accountActivated;
	}
	
	public BigDecimal getBalance() {
		return balance;
	}
	
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
    
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
}
