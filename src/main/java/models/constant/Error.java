package models.constant;

public enum Error {
	
	DB_UPDATE_FILED("Database update failed."),
	DB_DELETE_FILED("Database delete failed."),
	DB_EMPTY_TABLE("The database table is empty."),
	DB_CANT_FIND_DATA("There are now data in a table with such values."),
	DB_WRON_ID("Wrong input DAL first field value (should be not null and greater than 0)."),
	OPPONENT_IS_MISSING("Cannot find opponent."),
	ITEM_WRONG_ID("Wrong input parameter. Item Id should be not less than one."),
	ITEM_WRONG_MIN_CHARACTER_LEVEL("Wrong input parameter. minCharacterLevel should be not less than one."),
	ITEM_WASNT_DOWNLOADED_FROM_DB("Item wasn't downloaded from the database."),
	WRONG_DEFAULT_IMAGE_ID("Wrong default image Id."),
	USER_HAS_NO_IMAGE("User has no image."),
	LOG_FILE_IS_EMPTY("Log file is empty."),
	NO_LOGS_AT_CURRENT_DATE("There are no logs at a current date.");

	private String _message;

	private Error(String message) {
		_message = message;
	}

	public String getMessage() {
		return _message;
	}

}
