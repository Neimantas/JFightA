package models.dto;

import models.dal.UserDAL;

public class UserDTO {

	public boolean success;
	public String message;
	public UserDAL userDal;

	public UserDTO() {
	}

	public UserDTO(boolean inputSuccess, String inputMessage, UserDAL inputUserDal) {
		success = inputSuccess;
		message = inputMessage;
		userDal = inputUserDal;
	}
}
