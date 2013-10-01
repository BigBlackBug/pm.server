package org.qbix.pm.server.interfaces;

import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.qbix.pm.server.dto.ResultInfo;
import org.qbix.pm.server.dto.GameDTO;
import org.qbix.pm.server.dto.UserJoinDTO;
import org.qbix.pm.server.exceptions.PMException;

//TODO change REST requests samples !
@Path("/")
public interface ClientAPI {

//	 http://localhost:8080/pm.server/rs/regsess
//	 {
//	 "stake" : 200.0,
//	 "vc" : { "parserId" : 0 },
//	 "type" : "LOL",
//	 "playerEntries" : [
//	 {"accountId" : 1} , {"accountId" : 2, "stake" : 200 }
//	 ]
//	 }
	@POST
	@Path("/register_game")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Long registerGame(GameDTO gameDTO) throws PMException;

	// http://localhost:8080/pm.server/rs/update_session
	// {
	// "sessid" : 100,
	// "stake" : 200.0,
	// "vc" : { "parserId" : 0 },
	// }
	@POST
	@Path("/update_game")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Long updateGame(GameDTO gameDTO) throws PMException;

	// http://localhost:8080/pm.server/rs/leave_game
	// {
	// { "sessid" : 249, "accountid" : 1 }
	@POST
	@Path("/player_disconnected")
	@Consumes(MediaType.APPLICATION_JSON)
	public void playerDisconnected(UserJoinDTO userDTO) throws PMException;

	// http://localhost:8080/pm.server/rs/confpart
	// { "sessid" : 249, "accountid" : 1 }
	@POST
	@Path("/confpart")
	@Consumes(MediaType.APPLICATION_JSON)
	public void confirmParticipation(UserJoinDTO userDTO) throws PMException;

	// http://localhost:8080/pm.server/rs/confpart
	// { "sessid" : 249, "accountid" : 1 }
	@POST
	@Path("/cancelpart")
	@Consumes(MediaType.APPLICATION_JSON)
	public void cancelParticipation(UserJoinDTO userDTO) throws PMException;
	
	@POST
	@Path("/cancel_game")
	@Consumes(MediaType.APPLICATION_JSON)
	public void cancelGame(GameDTO gameDTO) throws PMException;

	// http://localhost:8080/pm.server/rs/startsess
	// { "sessid" : 249 }
	@POST
	@Path("/start_game")
	@Consumes(MediaType.APPLICATION_JSON)
	public void startGame(GameDTO gameDTO) throws PMException;

	// http://localhost:8080/pm.server/rs/resolveresult
	// { "sessid" : 258, "winner" : 0 }
	@POST
	@Path("/resolve_result")
	@Consumes(MediaType.APPLICATION_JSON)
	public void resolveResult(ResultInfo resultInfo) throws PMException;

	/**
	 * Возвращает мапу LOL_ID -> PM_ID. Если в ключе мапы нет переданного
	 * идентификатора, то значит у пользователя нет аккаунта ПМ
	 */
	@POST
	@Path("/get_pmid_by_lolid")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Map<Long, Long> getPmIdByLolId(List<Long> ids) throws PMException;

}
