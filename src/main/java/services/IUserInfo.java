package services;

import javax.servlet.http.HttpServletRequest;

import models.business.Player;
import models.dto.ObjectDTO;

public interface IUserInfo {

	ObjectDTO<Player> getUserInfo(int userId);
	ObjectDTO<Player> getLoggedUserInfo(HttpServletRequest request);
	void setNotReady(int userId);
	
}
