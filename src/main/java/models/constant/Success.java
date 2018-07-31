package models.constant;

public enum Success {

	SUCCESS("Success."),
	DB_CREATE_SUCCESSFUL("New table row created."),
	DB_READ_SUCCESSFUL("Read successful."),
	DB_UPDATE_SUCCESSFUL("Update successful."),
	DB_DELETE_SUCCESSFUL("Delete successful."),
	ITEM_IS_TAKEN_FROM_CACHE("Item is taken from the cache."),
	IMAGE_IS_TAKEN_FROM_CACHE("Image is taken from the cache."),
	USER_HAS_AN_IMAGE("User has an image.");

	private String _message;

	private Success(String message) {
		_message = message;
	}

	public String getMessage() {
		return _message;
	}
	
}
