package org.qbix.pm.server.model;

public enum SessionStatus {

	DOES_NOT_EXIST,

	REGISTERED,

	ACCEPTING_PLAYERS,
	
	READY_FOR_POLLING,

	POLLING,

	RESULT_READY,

	CLOSED;
}
