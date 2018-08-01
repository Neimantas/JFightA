package services;

import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.PlayerDalDTO;
import models.dto.UserLoginDataDTO;

public interface IHigherService {
	PlayerDalDTO login(UserLoginData user);
	UserLoginDataDTO registration(UserRegIn userRegIn);
	String hashPassword (String password);
	boolean passwordCheck(String password, String paswordInDB);
	Integer getNewFightId();
	void writeFightResult(int fightId, int winPlayerId, int losePlayerId, boolean draw);
}