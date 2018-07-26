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
import models.dto.PlayerDTO;
import models.dto.PlayerDalDTO;
import models.dto.UserFrontDTO;
import services.ICache;
import services.ILoginService;

public class LoginService implements ILoginService {

	HigherLoginService hService;
	ICache cashe;

	public LoginService(HigherLoginService hServiceImpl) {
		hService = hServiceImpl;
		cashe = CacheImpl.getInstance();
	}

	@Override
	public PlayerDTO login(HttpServletResponse response, UserLoginData userIn) {
		HigherLoginService hlog = new HigherLoginService();
		User userOut = new User();
		// Get player form hService
		PlayerDalDTO playerDalDto = hlog.login(userIn);
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
			return new PlayerDTO(true, "success", player);
		}
		return new PlayerDTO(false, playerDalDto._message, null);
	}

	@Override
	public UserFrontDTO registration(UserRegIn userRegIn) {
		return null;
	}

	@Override
	public void logout(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		String cookieValue = "";
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("JFightUser")) {
				cookieValue = cookies[i].getValue();
				cookies[i].setMaxAge(0);
			}
		}
		for (Entry<Integer, Player> entry : cashe.getPlayers().entrySet()) {
			if (entry.getValue().user.cookiesValue.equals(cookieValue)) {
				cashe.removePlayer(entry.getKey());
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
		cashe.addPlayer(userId, player);
		cashe.getPlayer(userId);
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
		for (Entry<Integer, Player> entry : cashe.getPlayers().entrySet()) {
			if (entry.getValue().user.cookiesValue.equals(cookieValue)) {
				return true;
			}
		}
		return false;
	}
}

