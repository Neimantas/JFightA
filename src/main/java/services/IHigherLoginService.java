package services;

import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.PlayerDalDTO;
import models.dto.UserLoginDataDTO;

public interface IHigherLoginService {
	PlayerDalDTO login(UserLoginData user);
	UserLoginDataDTO registration(UserRegIn userRegIn);
}
