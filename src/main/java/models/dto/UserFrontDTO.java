package models.dto;

import models.business.User;

public class UserFrontDTO {

	public boolean success;
	public String message;
	public User user;

	public UserFrontDTO() {
	}

	public UserFrontDTO(boolean inputSuccess, String inputMessage, User inputUser) {
		success = inputSuccess;
		message = inputMessage;
		user = inputUser;
	}
}
