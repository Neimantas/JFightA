package models.dto;

import models.business.UserLoginData;

public class UserLoginDataDTO {
	public boolean success;
	public String message;
	UserLoginData userloginData;

	public UserLoginDataDTO(){
	}
	public UserLoginDataDTO(boolean _success, String _message, UserLoginData _userloginData){
		success=_success;
		message=_message;
		userloginData=_userloginData;
	}
}

