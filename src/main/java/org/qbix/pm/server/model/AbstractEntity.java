package org.qbix.pm.server.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@GeneratedValue
	@Id
	private Long id;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AbstractEntity)) {
			return false;
		}
		AbstractEntity ae = (AbstractEntity) obj;
		return ae.getId() == null ? false : ae.getId().equals(getId());
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	
}
