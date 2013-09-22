package org.qbix.pm.server.model;

public enum Team {
	TEAM_0(0),

	TEAM_1(1);

	private int code;

	private Team(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static Team valueOf(int code) throws IllegalArgumentException {
		switch (code) {
		case 0:
			return TEAM_0;
		case 1:
			return TEAM_1;
		}

		throw new IllegalArgumentException(
				"No SessionTeam enum value for value = " + code);
	}

}