package models.constant;

public enum Error {
	
	DB_UPDATE_FILED("Database update failed."),
	DB_DELETE_FILED("Database delete failed."),
	DB_EMPTY_TABLE("The database table is empty."),
	DB_CANT_FIND_DATA("There are now data in a table with such values."),
	DB_WRON_ID("Wrong input DAL first field value (should be not null and greater than 0)."),
	OPPONENT_IS_MISSING("Cannot find opponent."),
	ITEM_WRONG_ID("Wrong input parameter. Item Id should be not less than one."),
	ITEM_EMPTY_NAME("Wrong input parameter. Item name should be not null and not empty."),
	ITEM_WRONG_IMAGE_FORMAT("Wrong input parameter. Incorrect image format."),
	ITEM_TYPE_IS_NOT_SETTED("Wrong input parameter. Incorrect item type"),
	ITEM_WRONG_MIN_CHARACTER_LEVEL("Wrong input parameter. minCharacterLevel should be not less than one."),
	ITEM_WASNT_DOWNLOADED_FROM_DB("Item wasn't downloaded from the database."),
	USER_WASNT_DOWNLOADED_FROM_DB("User wasn't downloaded from the database."),
	USER_HAS_NO_IMAGE("User has no image."),
	IMAGE_IS_NOT_SETTED("Wrong input parameter. Image is not setted."),
	IMAGE_WRONG_USER_ID("Wrong input parameter. Item Id should be not less than one."),
	IMAGE_EMPTY_NAME("Wrong input parameter. Image name should be not null and not empty."),
	IMAGE_TYPE_IS_NOT_SETTED("Wrong input parameter. Incorrect image type"),
	DEFAULT_IMAGE_USER_ID_MUST_BE_ZERO("Wrong input parameter. Default image user Id must be zero."),
	WRONG_DEFAULT_IMAGE_ID("Wrong default image Id."),
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
