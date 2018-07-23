package services;

import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.DTO;
import models.dto.PlayerDalDTO;
import models.dto.UserDTO;

public interface IHigherLoginService {
	PlayerDalDTO login(UserLoginData user);
	PlayerDalDTO registration(UserRegIn userRegIn);
}
