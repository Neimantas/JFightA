package services;

import models.business.User;
import models.dto.UserFrontDTO;

public interface ILoginService {
	UserFrontDTO login(User user);
}
