package services;

import models.business.Item;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.ItemDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;
import models.dto.PlayerDalDTO;
import models.dto.UserLoginDataDTO;

public interface IHigherService {
	PlayerDalDTO login(UserLoginData user);
	UserLoginDataDTO registration(UserRegIn userRegIn);
	String hashPassword (String password);
	boolean passwordCheck(String password, String paswordInDB);
	Integer getNewFightId();
	ObjectDTO<ItemDAL> getItem(int itemId);
	ObjectDTO<Integer> createNewItem(Item item);
	DTO editItem(Item item);
	DTO deleteItem(Item item);
}