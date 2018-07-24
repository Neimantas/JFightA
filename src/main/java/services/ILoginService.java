package services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.business.Player;
import models.business.User;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.PlayerDTO;
import models.dto.UserFrontDTO;

public interface ILoginService {
	PlayerDTO login(HttpServletResponse response,UserLoginData user);
	UserFrontDTO registration(UserRegIn userRegIn);
	void addCookies(HttpServletResponse response,User userWithInfo);
	void aadCashe(Player player, int useId);
	boolean userValidaro (HttpServletRequest request);
	void logout (User user);
	String newGuid();
}

