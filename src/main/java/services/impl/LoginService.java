package services.impl;

import java.util.Map.Entry;
import java.util.UUID;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import models.business.CharacterInfo;
import models.business.Player;
import models.business.User;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.UserDAL;
import models.dto.DTO;
import models.dto.PlayerDTO;
import models.dto.PlayerDalDTO;
import models.dto.UserFrontDTO;
import models.dto.UserLoginDataDTO;
import services.ICache;
import services.ILoginService;

public class LoginService implements ILoginService {

	HigherLoginService hService;
	ICache cache;

	public LoginService(HigherLoginService hServiceImpl) {
		hService = hServiceImpl;
		cache = CacheImpl.getInstance();
	}

	@Override
	public DTO login(HttpServletResponse response, UserLoginData userIn) {
		User userOut = new User();
		// Get player form hService
		PlayerDalDTO playerDalDto = hService.login(userIn);
		if (playerDalDto._success) {
			UserDAL userInDal = playerDalDto._playerDal.userDal;
			// Transforms userDal to user
			userOut.userId = userInDal.userId;
			userOut.name = userInDal.name;
			userOut.accessLevel = userInDal.accessLevel;
			userOut.imageId = userInDal.imageId;
			userOut.eMail = userInDal.eMail;
			// Creates new guide and add it to cookies value
			userOut.cookiesValue = newGuid();
			addCookies(response, userOut);
			// Transforms charcterDal to characterInfo
			CharacterInfo charInfo = new CharacterInfo();
			charInfo.experience = playerDalDto._playerDal.characterDal.experience;
			charInfo.healthPoints = playerDalDto._playerDal.characterDal.healthPoints;
			charInfo.level = playerDalDto._playerDal.characterDal.level;
			charInfo.strenght = playerDalDto._playerDal.characterDal.strenght;
			charInfo.userId = playerDalDto._playerDal.characterDal.userId;
			charInfo.attackItemId = playerDalDto._playerDal.characterDal.attackItemId;
			charInfo.defenceItemId = playerDalDto._playerDal.characterDal.defenceItemId;
			// adds user and character to player
			Player player = new Player();
			player.characterInfo = charInfo;
			player.user = userOut;
			// add player to cache
			aadCashe(player, userOut.userId);
			return new DTO(true, "success");
		}
		return new DTO(false, playerDalDto._message);
	}

	@Override
	public UserLoginDataDTO registration(UserRegIn userRegIn) {
		UserLoginDataDTO userloginData = hService.registration(userRegIn);
		if (userloginData.success) {
			UserLoginData loginData = new UserLoginData();
			loginData.name = userloginData.userloginData.name;
			loginData.password = userloginData.userloginData.password;
			return new UserLoginDataDTO(true, "success", loginData);
		}
		return new UserLoginDataDTO(false, userloginData.message, null);
	}

	@Override
	public void logout(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String cookieValue = "";
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("JFightUser")) {
				cookieValue = cookies[i].getValue();
				cookies[i].setMaxAge(0);
				break;
			}
		}
		for (Entry<Integer, Player> entry : cache.getPlayers().entrySet()) {
			if (entry.getValue().user.cookiesValue.equals(cookieValue)) {
				if (entry.getValue().user.imageId != null) {
					cache.removeImage(entry.getValue().user.imageId);
				}
				cache.removePlayer(entry.getKey());
				break;
			}
		}

	}

	@Override
	public void addCookies(HttpServletResponse response, User userWithInfo) {
		Cookie ck = new Cookie("JFightUser", userWithInfo.cookiesValue);
		ck.setMaxAge(3600);
		response.addCookie(ck);

	}

	@Override
	public String newGuid() {
		UUID uuid = UUID.randomUUID();
		String cookiesValue = uuid.toString();
		return cookiesValue;
	}

	@Override
	public void aadCashe(Player player, int userId) {
		cache.addPlayer(player);
		cache.getPlayer(userId);
	}

	@Override
	public boolean userValidator(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String cookieValue = "";
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("JFightUser")) {
				cookieValue = cookies[i].getValue();
				cookies[i].setMaxAge(3600);
			}
		}
		for (Entry<Integer, Player> entry : cache.getPlayers().entrySet()) {
			if (entry.getValue().user.cookiesValue.equals(cookieValue)) {
				return true;
			}
		}
		return false;
	}
}
