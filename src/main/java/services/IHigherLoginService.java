package services;

import models.dal.UserDAL;
import models.dto.UserDTO;

public interface IHigherLoginService {
	UserDTO login(UserDAL userDal);
}
