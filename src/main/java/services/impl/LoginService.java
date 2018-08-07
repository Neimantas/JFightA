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
import models.dal.PlayerDAL;
import models.dal.UserDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;
import services.ICache;
import services.ILoginService;

public class LoginService implements ILoginService {

	HigherService hService;
	ICache cache;

	public LoginService(HigherService hServiceImpl) {
		hService = hServiceImpl;
		cache = CacheImpl.getInstance();
	}

	@Override
	public DTO login(HttpServletResponse response, UserLoginData userIn) {
		User userOut = new User();
		// Get player form hService
		ObjectDTO<PlayerDAL> playerDalDto = hService.login(userIn);
		if (playerDalDto.success) {
			UserDAL userInDal = playerDalDto.transferData.userDal;
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
			charInfo.experience = playerDalDto.transferData.characterDal.experience;
			charInfo.healthPoints = playerDalDto.transferData.characterDal.healthPoints;
			charInfo.level = playerDalDto.transferData.characterDal.level;
			charInfo.strenght = playerDalDto.transferData.characterDal.strenght;
			charInfo.userId = playerDalDto.transferData.characterDal.userId;
			charInfo.attackItemId = playerDalDto.transferData.characterDal.attackItemId;
			charInfo.defenceItemId = playerDalDto.transferData.characterDal.defenceItemId;
			// adds user and character to player
			Player player = new Player();
			player.characterInfo = charInfo;
			player.user = userOut;
			// add player to cache
			aadCashe(player);
			return new DTO(true, "success");
		}
		return new DTO(false, playerDalDto.message);
	}

	@Override
	public ObjectDTO<UserLoginData> registration(UserRegIn userRegIn) {
		ObjectDTO<UserLoginData> userloginData = hService.registration(userRegIn);
		if (userloginData.success) {
			UserLoginData loginData = new UserLoginData();
			loginData.name = userloginData.transferData.name;
			loginData.password = userloginData.transferData.password;
			return new ObjectDTO<UserLoginData>(true, "success", loginData);
		}
		return new ObjectDTO<UserLoginData>(false, userloginData.message, null);
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String cookieValue = "";
		for (int i = cookies.length - 1; i >= 0; i--) {
			if (cookies[i].getName().equals("JFightUser")) {
				System.out.println("pateko i cookies trinima");
				cookieValue = cookies[i].getValue();
				cookies[i].setMaxAge(0);
				response.addCookie(cookies[i]);
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
		ck.setMaxAge(86400);
		response.addCookie(ck);
	}

	@Override
	public String newGuid() {
		UUID uuid = UUID.randomUUID();
		String cookiesValue = uuid.toString();
		return cookiesValue;
	}

	@Override
	public void aadCashe(Player player) {
		cache.addPlayer(player);
	}

	@Override
	public boolean userValidator(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			return false;
		String cookieValue = "";
		for (int i = cookies.length - 1; i >= 0; i--) {
			if (cookies[i].getName().equals("JFightUser")) {
				cookieValue = cookies[i].getValue();
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
