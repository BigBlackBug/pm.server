package org.qbix.pm.server.beans;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.qbix.pm.server.dto.SessionInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMException;

@Path("/")
public interface ClientAPI {

	@POST
	@Path("/regsess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Long registerSession(SessionInfo si) throws PMException;

	@POST
	@Path("/startconf")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	/* Должно быть заполнено поле sessid */
	public void startPlayersConfirmation(SessionInfo si)
			throws PMException;

	@POST
	@Path("/confpart")
	@Consumes(MediaType.APPLICATION_JSON)
	public void confirmParticipation(UserJoinInfo uji) throws PMException;

	@POST
	@Path("/cancelpart")
	@Consumes(MediaType.APPLICATION_JSON)
	public void cancelParticipation(UserJoinInfo uji) throws PMException;

	/** if session requires manual start command */
	@POST
	@Path("/startsess")
	@Consumes(MediaType.APPLICATION_JSON)
	public void startSession(SessionInfo si) throws PMException;
	
}
