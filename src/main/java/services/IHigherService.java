package services;

import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.FightDataDAL;
import models.dal.LogDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dal.UserDAL;
import models.dto.DTO;

public interface IHigherService {
	ObjectDTO login(UserLoginData user);
	ObjectDTO<UserLoginData> registration(UserRegIn userRegIn);
	String hashPassword (String password);
	boolean passwordCheck(String password, String paswordInDB);
	Integer getNewFightId();
	ListDTO<FightDataDAL> getFightData(int fightId);
	void writeFightResult(int fightId, int winPlayerId, int losePlayerId, boolean draw);
	ListDTO<LogDAL> logInfo(int userIdA, int userIdB);
	ObjectDTO<ItemDAL> getItem(int itemId);
	ObjectDTO<Integer> logFightData(int fightId, int userIdA, int userIdB, String json);
	ObjectDTO<Integer> createNewItem(ItemDAL itemDAL);
	DTO editItem(ItemDAL itemDAL);
	DTO deleteItem(ItemDAL itemDAL);
	ObjectDTO<UserDAL> getUser(int userId);
	ObjectDTO<UserDAL> updateUserImageId(int userId, int imageId);
	ObjectDTO<ImageDAL> getImage(int imageId);
	ObjectDTO<Integer> getImageId(int userId);
	ObjectDTO<Integer> createNewImage(ImageDAL imageDAL);
	DTO updateImage(ImageDAL imageDAL);
	DTO deleteImage(ImageDAL imageDAL);
	DTO deleteFightData(int fightId);
}