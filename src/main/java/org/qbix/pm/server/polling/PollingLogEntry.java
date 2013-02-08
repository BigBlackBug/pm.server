package org.qbix.pm.server.polling;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.qbix.pm.server.model.EntityWithSerializedParams;

@Entity
public class PollingLogEntry extends EntityWithSerializedParams{
	
	private static final long serialVersionUID = 6929117625215244642L;

	@Transient
	private transient ReturnCode returnCode;
	
	@Column(name = "return_code")
	private int statusCode;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	@PrePersist
    void prePersist(){
        statusCode = returnCode.getCode();
    }

    @PostLoad
    void postLoad(){
    	returnCode = ReturnCode.valueOf(statusCode);
    }
    
    public ReturnCode getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(ReturnCode returnCode) {
		this.returnCode = returnCode;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
}
