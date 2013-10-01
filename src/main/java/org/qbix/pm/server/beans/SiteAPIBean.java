package org.qbix.pm.server.beans;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;

import org.qbix.pm.server.annotaions.Traceable;
import org.qbix.pm.server.dto.UserAccountDTO;
import org.qbix.pm.server.dto.UserGamesHistoryDTO;
import org.qbix.pm.server.exceptions.PMException;
import org.qbix.pm.server.exceptions.PMValidationException;
import org.qbix.pm.server.interfaces.SiteClientAPI;
import org.qbix.pm.server.model.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Traceable
public class SiteAPIBean extends AbstractBean implements SiteClientAPI {

	private static Logger log = LoggerFactory.getLogger(SiteAPIBean.class);

	@Override
	public Long createOrUpdateUserAccount(UserAccountDTO userAccountDTO)
			throws PMException {
		UserAccount userAccount = new UserAccount();
		if (userAccountDTO.getID() != null) {
			userAccount = lockEntity(UserAccount.class, userAccountDTO.getID());
		} 
		
		checkNick(userAccountDTO.getNickName());
		checkEmail(userAccountDTO.getEmail());
		
		userAccount.setID(userAccountDTO.getID());
		userAccount.setNickName(userAccountDTO.getNickName());
		userAccount.setEmail(userAccountDTO.getEmail());
		userAccount.setPassword(userAccountDTO.getPassword());

		userAccount = em.merge(userAccount);
		return userAccount.getID();
	}

	private void checkNick(String nickName) throws PMException {
		if (nickName != null && getUserAccount(nickName, null, null) != null) {
			throw new PMValidationException(
					"Уже существует пользвателель с таким ником");
		}
	}
	
	private void checkEmail(String email) throws PMException {
		//TODO
	}

	@Override
	public void deleteUserAccount(UserAccountDTO userAccount)
			throws PMException {
		// TODO Auto-generated method stub
		log.info("deleting acc " + userAccount.getID());
	}

	@Override
	public UserGamesHistoryDTO getGamesHistory(Long accountId)
			throws PMException {
		// TODO Auto-generated method stub
		return new UserGamesHistoryDTO();
	}

	private UserAccountDTO convertToDto(UserAccount acc) {
		UserAccountDTO dto = new UserAccountDTO();
		dto.setID(acc.getID());
		dto.setBalance(acc.getBalance());
		dto.setEmail(acc.getEmail());
		dto.setNickName(acc.getNickName());
		dto.setPassword(acc.getPassword());
		return dto;
	}

	@Override
	public UserAccountDTO getUserAccount(String nick, String password,
			Long accountId) throws PMException {

		if (accountId != null) {
			return convertToDto(em.getReference(UserAccount.class, accountId));
		}

		try {
			if (nick != null && password == null) {
				UserAccount result = em
						.createQuery(
								"SELECT DISTINCT ua FROM "
										+ UserAccount.class.getName()
										+ " ua where ua.nickName = :nickname",
								UserAccount.class)
						.setParameter("nickname", nick).getSingleResult();

				return convertToDto(result);
			}

			if (nick != null && password != null) {
				UserAccount result = em
						.createQuery(
								"SELECT DISTINCT ua FROM "
										+ UserAccount.class.getName()
										+ " ua where ua.nickName = :nickname"
										+ " AND ua.password = :password",
								UserAccount.class)
						.setParameter("nickname", nick)
						.setParameter("password", password).getSingleResult();

				return convertToDto(result);
			}
		} catch (NoResultException nre) {
			return null;
		}

		throw new IllegalArgumentException();
	}
}
