package services;

import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.UserDTO;


public interface IHigherLoginService {
	UserDTO login(UserLoginData user);
	UserDTO registration(UserRegIn userRegIn);
}
