package org.qbix.pm.server.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@GeneratedValue
	@Id
	private Long ID;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	public void setID(Long id) {
		this.ID = id;
	}

	public Long getID() {
		return ID;
	}

	protected void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	@PrePersist
	void setCreationDate() {
		setCreationDate(new Date());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractEntity)) {
			return false;
		}

		if (this == obj) {
			return true;
		}

		AbstractEntity ae = (AbstractEntity) obj;
		return ae.getID() == null ? false : ae.getID().equals(getID());
	}

	@Override
	public int hashCode() {
		if (ID != null) {
			return ID.hashCode();
		}
		return super.hashCode();
	}

}
