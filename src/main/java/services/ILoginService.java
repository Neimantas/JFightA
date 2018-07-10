package services;

import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.UserFrontDTO;

public interface ILoginService {
	UserFrontDTO login(UserLoginData user);
	UserFrontDTO registration(UserRegIn userRegIn);
}

