package services.impl;

import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import configuration.StartupContainer;
import models.business.Player;
import models.dal.CharacterDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.ICache;
import services.IUserInfo;

public class UserInfoImpl implements IUserInfo {

	private ICRUD _crud;
	private CharacterDAL _cDAL;
	private ICache _cache;

	public UserInfoImpl(CRUDImpl crud) {
		_crud = crud;
		_cache = CacheImpl.getInstance();
	}

	@Override
	public ObjectDTO<CharacterDAL> getUserInfo(int userId) {

		CharacterDAL dal = new CharacterDAL();

		dal.userId = userId;

		ListDTO<CharacterDAL> dto = _crud.<CharacterDAL>read(dal);
		if (dto.success) {

			_cDAL = dto.transferDataList.get(0);

			ObjectDTO<CharacterDAL> retSuccess = new ObjectDTO();
			retSuccess.success = true;
			retSuccess.message = "User exists in DB."; // Needs ENUM
			retSuccess.transferData = _cDAL;

			return retSuccess;
		}

		ObjectDTO<CharacterDAL> retFailure = new ObjectDTO();
		retFailure.success = false;
		retFailure.message = "Error! No such user."; // Needs ENUM

		return retFailure;
	}

	public ObjectDTO<Player> getCacheUserInfo(HttpServletRequest request) {
		Player player = new Player();
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
				player = _cache.getPlayer(entry.getValue().user.userId);

				ret.success = true;
				ret.message = "User exists in DB."; // Needs ENUM
				ret.transferData = player;
			} else {
				ret.success = false;
				ret.message = "Error! No such user."; // Needs ENUM
			}

		}
		return ret;
	}

}
