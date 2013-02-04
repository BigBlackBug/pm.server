package org.qbix.pm.server.model;

public enum SessionStatus {

	NOT_EXIST,

	REGISTERED,

	PLAYERS_CONFIRMATION,

	POLLING,

	ANALYZING_RESULT,
	
	PREPARING_TO_TRANSFER, 

	TRANSFERRING_MONEY,

	CLOSED;
}
