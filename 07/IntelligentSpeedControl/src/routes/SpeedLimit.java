package routes;

public enum SpeedLimit {
	ZERO(0), EIGHTY(80), FIFTY(50), HUNDERTTWENTY(120), NONE(Integer.MAX_VALUE), THIRTY(30), FIVE(5);

	private int limit = Integer.MAX_VALUE;

	private SpeedLimit(int limit) {
		this.limit = limit;
	}

	public int getLimit() {
		return limit;
	}
}
