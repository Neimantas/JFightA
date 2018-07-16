package models.constant;

public enum UserStatus {

	NOT_READY(1, "NotReady"),
	READY(2, "Ready"),
	PLAYING(3, "Playing");

	private int _statusValue;
	private String _statusName;

	private UserStatus(int userStatusValue, String userStatusName) {
		_statusValue = userStatusValue;
		_statusName = userStatusName;
	}

	public static UserStatus getByStatusValue(int userStatusValue) {
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getStatusValue() == userStatusValue) {
				return userStatus;
			}
		}
		return null;
	}

	public static UserStatus getByStatusName(String userStatusName) {
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getStatusName().equalsIgnoreCase(userStatusName)) {
				return userStatus;
			}
		}
		return null;
	}

	public int getStatusValue() {
		return _statusValue;
	}

	public String getStatusName() {
		return _statusName;
	}

}
