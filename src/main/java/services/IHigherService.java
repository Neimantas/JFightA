package services;

import models.business.Item;
import models.business.ProfileImage;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dal.UserDAL;
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
	DTO deleteItem(int itemId);
	ObjectDTO<UserDAL> getUser(int userId);
	ObjectDTO<UserDAL> updateUserImageId(int userId, int imageId);
	ObjectDTO<ImageDAL> getImage(int imageId);
	ObjectDTO<Integer> getImageId(int userId);
	ObjectDTO<Integer> createNewImage(ProfileImage profileImage);
	DTO updateImage(ProfileImage profileImage);
	DTO deleteImage(int imageId);
}