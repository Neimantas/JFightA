package models.constant;

public enum AccessLevel {

	USER(1, "User", true, true, false, false, false, false),
	ADVANCED_USER(2, "AdvancedUser", true, true, true, false, false, false),
	MODERATOR(3, "Moderator", true, true, true, true, true, false),
	ADMINISTRATOR(4, "Administrator", true, true, true, true, true, true);

	private int _accessLevelValue;
	private String _accessLevelName;
	private boolean _editOwnAccount;
	private boolean _sendChatMessage;
	private boolean _giveBonusPoints;
	private boolean _sendMessageForAllUsers;
	private boolean _editOtherAccounts;
	private boolean _giveOrRemoveAdminAndModeratorRights;

	private AccessLevel(int value, String name, boolean editMyAccount, boolean sendMessage, boolean givePoinst,
			boolean sendMessageForAll, boolean editAccounts, boolean giveAdmModRights) {
		_accessLevelValue = value;
		_accessLevelName = name;
		_editOwnAccount = editMyAccount;
		_sendChatMessage = sendMessage;
		_giveBonusPoints = givePoinst;
		_sendMessageForAllUsers = sendMessageForAll;
		_editOtherAccounts = editAccounts;
		_giveOrRemoveAdminAndModeratorRights = giveAdmModRights;
	}

	public static AccessLevel getByAccessValue(int accessValue) {
		for (AccessLevel accessLevel : AccessLevel.values()) {
			if (accessLevel.getValue() == accessValue) {
				return accessLevel;
			}
		}
		return null;
	}

	public static AccessLevel getByAccessName(String accessName) {
		for (AccessLevel accessLevel : AccessLevel.values()) {
			if (accessLevel.getName().equalsIgnoreCase(accessName)) {
				return accessLevel;
			}
		}
		return null;
	}

	public int getValue() {
		return _accessLevelValue;
	}

	public String getName() {
		return _accessLevelName;
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
