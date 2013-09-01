package org.qbix.pm.server.model;

public enum SessionStatus {

	DOES_NOT_EXIST,

	ACCEPTING_PLAYERS,
	
	READY_TO_START,

	SESSION_COMMITED,
	
	GAME_IN_PROGRESS,

	RESULT_READY,
	
	RESOLVING_RESULT,

	CLOSED;
}
