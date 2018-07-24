package services;

import models.dal.CharacterDAL;
import models.dto.ListDTO;

public interface IUserInfo {
	
	ListDTO<CharacterDAL> getUserInfo(int userId);

}
