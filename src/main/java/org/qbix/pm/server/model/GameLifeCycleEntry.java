package org.qbix.pm.server.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Entity
public class GameLifeCycleEntry extends AbstractEntity {

	private static final long serialVersionUID = -3615255025713052880L;

	@ManyToOne
	@NotNull
	private Game game;
	
	private GameStatus status;
	
	public void setGame(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public void setStatus(GameStatus status) {
		this.status = status;
	}
	
	public GameStatus getStatus() {
		return status;
	}
	
}
