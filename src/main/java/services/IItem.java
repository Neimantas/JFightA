package services;

import java.util.Map;

import models.constant.ItemType;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IItem {

	ObjectDTO<ImageDAL> getUserAImage(int userAId);
	ObjectDTO<ImageDAL> getUserBImage(int userBId);
	ObjectDTO<Integer> addImage(int userId, String imageName, byte[] image);
	DTO editImage(int userId, String imageName, byte[] image);
	DTO editDefaultImage(int imageId);
	DTO deleteImage(int userId);
	ObjectDTO<ItemDAL> getItem(int itemId);
	Map<ItemType, ItemDAL> getUserItems(int userId);

}
