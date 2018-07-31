package models.constant;

public enum TimeMs {
	
	SECOND(1000), MINUTE(60000), HOUR(3600000);
	
	private int _milliseconds;
	
	private TimeMs(int milliseconds) {
		_milliseconds = milliseconds;
	}

	public int getMilliseconds() {
		return _milliseconds;
	}

}
