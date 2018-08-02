package services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.business.Player;
import models.business.User;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.DTO;
import models.dto.UserLoginDataDTO;

public interface ILoginService {
	DTO login(HttpServletResponse response,UserLoginData user);
	UserLoginDataDTO registration(UserRegIn userRegIn);
	void addCookies(HttpServletResponse response,User userWithInfo);
	void aadCashe(Player player);
	boolean userValidator (HttpServletRequest request, HttpServletResponse response);
	void logout (HttpServletRequest request, HttpServletResponse response);
	String newGuid();
}

