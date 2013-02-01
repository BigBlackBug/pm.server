package org.qbix.pm.server.model;

public enum SessionStatus {

	NOT_EXIST,

	REGISTERED,

	PLAYERS_CONFIRMATION,

	POLLING,

	RESULT_ANALYZING,
	
	RESULT_ANALYZED, 

	MONEY_TRANSFERING,

	CLOSED;
}
