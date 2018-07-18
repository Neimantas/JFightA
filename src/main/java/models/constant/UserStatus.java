package models.constant;

public enum UserStatus {

	NOT_READY(1, "NotReady"),
	READY(2, "Ready"),
	PLAYING(3, "Playing");

	private int _statusValue;
	private String _statusTitle;

	private UserStatus(int userStatusValue, String userStatusTitle) {
		_statusValue = userStatusValue;
		_statusTitle = userStatusTitle;
	}

	public static UserStatus getByUserStatusValue(int userStatusValue) {
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getStatusValue() == userStatusValue) {
				return userStatus;
			}
		}
		return null;
	}

	public static UserStatus getByUserStatusTitle(String userStatusTitle) {
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getStatusTitle().equalsIgnoreCase(userStatusTitle)) {
				return userStatus;
			}
		}
		return null;
	}

	public int getStatusValue() {
		return _statusValue;
	}

	public String getStatusTitle() {
		return _statusTitle;
	}

}
