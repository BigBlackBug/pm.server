package org.qbix.pm.server.dto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;

import org.qbix.pm.server.model.PlayerEntry;
import org.qbix.pm.server.model.Game;
import org.qbix.pm.server.model.GameType;

//json obj
/**
 * <code>
 * {
	"ID" : 1, 
	
	"type" : "LOL",
	
	"stake" : 100.0, 

	"pr" : { "parserId" : 1, "requirements" : { "key1" : "val1" } },
	
	"vc" : { "parserId" : 10, "criteria" : { "key1" : 2 } }
	
	} </code>
 * 
 */
public class GameDTO extends AbstractDTO<Game> {

	private static final long serialVersionUID = 3478578098981496431L;

	private Long ID;

	/** "LOL" , "HON" */
	private String type;

	private VictoryCriteriaDTO vc;

	private BigDecimal stake;

	private List<PlayerEntryDTO> playerEntries;

	private Integer status;

	public GameDTO() {
	}

	public GameDTO(Long id) {
		ID = id;
	}

	public void setID(Long sessionId) {
		this.ID = sessionId;
	}

	public Long getID() {
		return ID;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setVc(VictoryCriteriaDTO vc) {
		this.vc = vc;
	}

	public VictoryCriteriaDTO getVc() {
		return vc;
	}

	public void setStake(BigDecimal stake) {
		this.stake = stake;
	}

	public BigDecimal getStake() {
		return stake;
	}

	public void setPlayerEntries(List<PlayerEntryDTO> playerInfos) {
		this.playerEntries = playerInfos;
	}

	public List<PlayerEntryDTO> getPlayerEntries() {
		return playerEntries;
	}

	@Override
	public Game convertToEntity(EntityManager em) {
		Game game = new Game();
		if (ID != null) {
			game.setID(ID);
		}

		game.setType(GameType.LOL);
		game.setStake(stake);

		if (playerEntries != null && !playerEntries.isEmpty()) {
			for (PlayerEntryDTO pei : playerEntries) {
				PlayerEntry pe = pei.convertToEntity(em);
				pe.setGame(game);
				game.getPlayers().add(pe);
			}
		}

		if (vc != null) {
			game.setVictoryCriteria(vc.convertToEntity(em));
		}
		return game;
	}

}
