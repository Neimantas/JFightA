package services.impl;

import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import models.business.Player;
import models.dto.ObjectDTO;
import services.ICache;
import services.IUserInfo;

public class UserInfoImpl implements IUserInfo {

	private ICache _cache;

	public UserInfoImpl(CRUDImpl crud) {
		_cache = CacheImpl.getInstance();
	}

	@Override
	public ObjectDTO<Player> getUserInfo(int userId) {

		Player player = _cache.getPlayer(userId);
		if (player != null) {
			ObjectDTO<Player> retSuccess = new ObjectDTO();
			retSuccess.success = true;
			retSuccess.message = "User exists in DB.";
			retSuccess.transferData = player;

			return retSuccess;
		}

		ObjectDTO<Player> retFailure = new ObjectDTO();
		retFailure.success = false;
		retFailure.message = "Error! No such user.";

		return retFailure;
	}

	public ObjectDTO<Player> getLoggedUserInfo(HttpServletRequest request) {
		ObjectDTO<Player> ret = new ObjectDTO();

		Cookie[] cookies = request.getCookies();
		String cookieValue = "";
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("JFightUser")) {
				cookieValue = cookies[i].getValue();
			}
		}
		for (Entry<Integer, Player> entry : _cache.getPlayers().entrySet()) {
			if (entry.getValue().user.cookiesValue.equals(cookieValue)) {
				Player player = _cache.getPlayer(entry.getValue().user.userId);

				ret.success = true;
				ret.message = "User exists in cache.";
				ret.transferData = player;
				return ret;
			}

		}
		ret.success = false;
		ret.message = "Error! No cached user.";
		return ret;
	}

}
