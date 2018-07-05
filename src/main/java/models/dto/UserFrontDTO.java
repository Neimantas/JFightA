package models.dto;

import models.business.User;


public class UserFrontDTO {
	public boolean _success;
	public String _message;
	public User _user;
	
	public UserFrontDTO() {
	}

	public UserFrontDTO(boolean success, String message, User user) {
		_success = success;
		_message = message;
		_user = user;
	}
}
