package models.constant;

public enum AccessLevel {

	USER(1, "User", true, true, false, false, false, false),
	ADVANCED_USER(2, "AdvancedUser", true, true, true, false, false, false),
	MODERATOR(3, "Moderator", true, true, true, true, true, false),
	ADMINISTRATOR(4, "Administrator", true, true, true, true, true, true);

	private int accessLevelValue;
	private String accessLevelName;
	private boolean editOwnAccount;
	private boolean sendChatMessage;
	private boolean giveBonusPoints;
	private boolean sendMessageForAllUsers;
	private boolean editOtherAccounts;
	private boolean giveOrRemoveAdminAndModeratorRights;

	private AccessLevel(int value, String name, boolean editMyAccount, boolean sendMessage, boolean givePoinst,
			boolean sendMessageForAll, boolean editAccounts, boolean giveAdmModRights) {
		accessLevelValue = value;
		accessLevelName = name;
		editOwnAccount = editMyAccount;
		sendChatMessage = sendMessage;
		giveBonusPoints = givePoinst;
		sendMessageForAllUsers = sendMessageForAll;
		editOtherAccounts = editAccounts;
		giveOrRemoveAdminAndModeratorRights = giveAdmModRights;
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
		return accessLevelValue;
	}

	public String getName() {
		return accessLevelName;
	}

	public boolean canEditOwnAccount() {
		return editOwnAccount;
	}

	public boolean canSendChatMessage() {
		return sendChatMessage;
	}

	public boolean canGiveBonusPoints() {
		return giveBonusPoints;
	}

	public boolean canSendMessageForAllUsers() {
		return sendMessageForAllUsers;
	}

	public boolean canEditOtherAccounts() {
		return editOtherAccounts;
	}

	public boolean canGiveOrRemoveAdminAndModeratorRights() {
		return giveOrRemoveAdminAndModeratorRights;
	}

}
