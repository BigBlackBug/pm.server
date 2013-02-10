package org.qbix.pm.server.polling;

public enum ReturnCode {
	
	SUCCESS(0),
	
	REQUEST_TIMEOUT(-1),
	
	UNKNOWN_ERROR(-2),
	
	INVALID_DATA_RETURNED(-3);
	
	private int code;

	private ReturnCode(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}

	public static ReturnCode valueOf(int statusCode) throws IllegalArgumentException{
		int i;
		ReturnCode[] values = ReturnCode.values();
		for (i = 0; i < values.length; i++) {
			ReturnCode value=values[i];
			if(value.getCode()==statusCode){
				return value;
			}
		}
		if(i==values.length){
			return UNKNOWN_ERROR;
		}
		return null;
	}
}
