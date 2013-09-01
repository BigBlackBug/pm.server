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
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
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
		return ae.getId() == null ? false : ae.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		if (id != null) {
			return id.hashCode();
		}
		return super.hashCode();
	}

}
