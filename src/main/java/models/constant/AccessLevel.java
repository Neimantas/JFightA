package models.constant;

public enum AccessLevel {

	USER(1, "User", true, true, false, false, false, false),
	ADVANCED_USER(2, "AdvancedUser", true, true, true, false, false, false),
	MODERATOR(3, "Moderator", true, true, true, true, true, false),
	ADMINISTRATOR(4, "Administrator", true, true, true, true, true, true);

	private int _accessLevelValue;
	private String _accessLevelTitle;
	private boolean _editOwnAccount;
	private boolean _sendChatMessage;
	private boolean _giveBonusPoints;
	private boolean _sendMessageForAllUsers;
	private boolean _editOtherAccounts;
	private boolean _giveOrRemoveAdminAndModeratorRights;

	private AccessLevel(int value, String title, boolean editMyAccount, boolean sendMessage, boolean givePoinst,
			boolean sendMessageForAll, boolean editAccounts, boolean giveAdmModRights) {
		_accessLevelValue = value;
		_accessLevelTitle = title;
		_editOwnAccount = editMyAccount;
		_sendChatMessage = sendMessage;
		_giveBonusPoints = givePoinst;
		_sendMessageForAllUsers = sendMessageForAll;
		_editOtherAccounts = editAccounts;
		_giveOrRemoveAdminAndModeratorRights = giveAdmModRights;
	}

	public static AccessLevel getByAccessValue(int accessLevelValue) {
		for (AccessLevel accessLevel : AccessLevel.values()) {
			if (accessLevel.getAccessLevelValue() == accessLevelValue) {
				return accessLevel;
			}
		}
		return null;
	}

	public static AccessLevel getByAccessTitle(String accessLevelTitle) {
		for (AccessLevel accessLevel : AccessLevel.values()) {
			if (accessLevel.getAccessLevelTitle().equalsIgnoreCase(accessLevelTitle)) {
				return accessLevel;
			}
		}
		return null;
	}

	public int getAccessLevelValue() {
		return _accessLevelValue;
	}

	public String getAccessLevelTitle() {
		return _accessLevelTitle;
	}

	public boolean canEditOwnAccount() {
		return _editOwnAccount;
	}

	public boolean canSendChatMessage() {
		return _sendChatMessage;
	}

	public boolean canGiveBonusPoints() {
		return _giveBonusPoints;
	}

	public boolean canSendMessageForAllUsers() {
		return _sendMessageForAllUsers;
	}

	public boolean canEditOtherAccounts() {
		return _editOtherAccounts;
	}

	public boolean canGiveOrRemoveAdminAndModeratorRights() {
		return _giveOrRemoveAdminAndModeratorRights;
	}

}
