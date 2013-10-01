package org.qbix.pm.server.interfaces;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.qbix.pm.server.dto.UserAccountDTO;
import org.qbix.pm.server.dto.UserGamesHistoryDTO;
import org.qbix.pm.server.exceptions.PMException;

@Path("/")
public interface SiteClientAPI {

	@POST
	@Path("/userAccounts")
	@Consumes(MediaType.APPLICATION_JSON)
	public Long createOrUpdateUserAccount(UserAccountDTO userAccount)
			throws PMException;

	@DELETE
	@Path("/userAccounts")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteUserAccount(UserAccountDTO userAccount)
			throws PMException;

	@GET
	@Path("/userAccounts")
	@Produces(MediaType.APPLICATION_JSON)
	public UserAccountDTO getUserAccount(@QueryParam("nickName") String nickName,
			@QueryParam("password") String password,
			@QueryParam("accountId") Long accountId) throws PMException;

	@GET
	@Path("/gamesHistory")
	@Produces(MediaType.APPLICATION_JSON)
	public UserGamesHistoryDTO getGamesHistory(Long accountId)
			throws PMException;

}
