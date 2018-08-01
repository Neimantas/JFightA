package services;

import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.FightDataDAL;
import models.dal.LogDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import models.dto.PlayerDalDTO;
import models.dto.UserLoginDataDTO;

public interface IHigherService {
	PlayerDalDTO login(UserLoginData user);
	UserLoginDataDTO registration(UserRegIn userRegIn);
	String hashPassword (String password);
	boolean passwordCheck(String password, String paswordInDB);
	Integer getNewFightId();
	ListDTO<FightDataDAL> getFightDataDAL(int fightId);
	void writeFightResult(int fightId, int winPlayerId, int losePlayerId, boolean draw);
	ListDTO<LogDAL> logInfoDAL(int userIdA, int userIdB);
	ObjectDTO<Integer> logFightDataDAL(int fightId, int userIdA, int userIdB, String json);
}