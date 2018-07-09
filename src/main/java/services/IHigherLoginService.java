package services;

import models.business.UserLoginData;
import models.dto.UserDTO;

public interface IHigherLoginService {
	UserDTO login(UserLoginData user);
}
