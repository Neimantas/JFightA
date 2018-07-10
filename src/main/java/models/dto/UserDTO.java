package models.dto;

import models.dal.UserDAL;

public class UserDTO {
	public boolean _success;
	public String _message;
	public UserDAL _userDal;

	public UserDTO() {
	}

	public UserDTO(boolean success, String message, UserDAL userDal) {
		_success = success;
		_message = message;
		_userDal = userDal;
	}
}
