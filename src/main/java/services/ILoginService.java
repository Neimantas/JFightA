package services;

import models.business.UserLoginData;
import models.dto.UserFrontDTO;

public interface ILoginService {
	UserFrontDTO login(UserLoginData user);
}
