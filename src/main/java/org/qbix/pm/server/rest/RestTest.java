package org.qbix.pm.server.rest;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.qbix.pm.server.annotaions.Traceble;
import org.qbix.pm.server.beans.AbstractBean;
import org.qbix.pm.server.beans.MongoBean;
import org.qbix.pm.server.model.Ent;

@Stateless
@Traceble
@Path("/ents")
@Produces({ MediaType.APPLICATION_JSON })
//TODO remove
public class RestTest extends AbstractBean {

	@PersistenceContext(unitName = "pm")
	private EntityManager em;

	@EJB
	private MongoBean mb;
	
	@GET
	@Path("/")
	public List<Ent> getAllEnts() {
		return em.createQuery(
				"select ent from " + Ent.class.getSimpleName() + " ent",
				Ent.class).getResultList();
	}

	@GET
	@Path("/{id}")
	public Ent getEntById(@PathParam("id") Long id) {
		return findEnt(id);
	}

	@GET
	@Path("/get")
	public Ent getEntById2(@QueryParam("id") Long id) {
		if (id == null) {
			return null;
		}
		return findEnt(id);
	}

	private Ent findEnt(Long id) {
		try {
			return em
					.createQuery(
							"select ent from " + Ent.class.getSimpleName()
									+ " ent where ent.id = :id", Ent.class)
					.setParameter("id", id).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
