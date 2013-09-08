package org.qbix.pm.server.util.collection;

public enum PredicateType {
	ALL(new Action<Boolean, Boolean>() {

		public Boolean execute(Boolean... params) {
			boolean result = true;
			for (boolean b : params) {
				result &= b;
			}
			return result;
		}

	}), ANY(new Action<Boolean, Boolean>() {

		public Boolean execute(Boolean... params) {
			boolean result = false;
			for (boolean b : params) {
				result |= b;
			}
			return result;
		}

	});

	private Action<Boolean, Boolean> executable;

	private PredicateType(Action<Boolean, Boolean> executable) {
		this.executable = executable;
	}

	public boolean combine(boolean previousResult, boolean nextResult) {
		return executable.execute(previousResult, nextResult);
	}
}