package org.qbix.pm.server.beans;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.SessionInfo;
import org.qbix.pm.server.dto.UserJoinInfo;
import org.qbix.pm.server.exceptions.PMException;

@Path("/")
public interface ClientAPI {

	// http://localhost:8080/pm.server/rs/regsess
	// {
	// "stake" : 200.0,
	// "vc" : { "parserId" : 0 },
	// "type" : "LOL",
	// "playerInfos" : [
	// {"accountId" : 1} , {"accountId" : 2, "stake" : 200 }
	// ]
	// }
	@POST
	@Path("/regsess")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Long registerSession(SessionInfo si) throws PMException;

	// http://localhost:8080/pm.server/rs/confpart
	// { "sessid" : 249, "accountid" : 1 }
	@POST
	@Path("/confpart")
	@Consumes(MediaType.APPLICATION_JSON)
	public void confirmParticipation(UserJoinInfo uji) throws PMException;

	@POST
	@Path("/cancelpart")
	@Consumes(MediaType.APPLICATION_JSON)
	public void cancelParticipation(UserJoinInfo uji) throws PMException;

	// http://localhost:8080/pm.server/rs/startsess
	// { "sessid" : 249 }
	@POST
	@Path("/startsess")
	@Consumes(MediaType.APPLICATION_JSON)
	public void startSession(SessionInfo si) throws PMException;

	// http://localhost:8080/pm.server/rs/resolveresult
	// { "sessid" : 258, "winner" : 0 }
	@POST
	@Path("/resolveresult")
	@Consumes(MediaType.APPLICATION_JSON)
	public void resolveResult(ResultInfo si) throws PMException;

}
