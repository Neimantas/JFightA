package models.constant;

public enum UserStatus {

	NOT_READY(1, "NotReady"),
	READY(2, "Ready"),
	PLAYING(3, "Playing");

	private int statusValue;
	private String statusName;

	private UserStatus(int userStatusValue, String userStatusName) {
		statusValue = userStatusValue;
		statusName = userStatusName;
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
		return statusValue;
	}

	public String getStatusName() {
		return statusName;
	}

}
