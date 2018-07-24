package services;

import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.PlayerDalDTO;


public interface IHigherLoginService {
	PlayerDalDTO login(UserLoginData user);
	PlayerDalDTO registration(UserRegIn userRegIn);
}
