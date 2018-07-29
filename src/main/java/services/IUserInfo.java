package services;

import javax.servlet.http.HttpServletRequest;

import models.business.Player;
import models.dal.CharacterDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;

public interface IUserInfo {

	ListDTO<CharacterDAL> getUserInfo(int userId);

	ObjectDTO<Player> getCacheUserInfo(HttpServletRequest request);
}
