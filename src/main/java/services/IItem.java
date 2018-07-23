package services;

import java.util.Map;

import models.constant.ItemType;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dto.ObjectDTO;

public interface IItem {

	ObjectDTO<ImageDAL> getUserAImage(int userAId);
	ObjectDTO<ImageDAL> getUserBImage(int userBId);
	ObjectDTO<ItemDAL> getItem(int itemId);
	Map<ItemType, Integer> getItemPoints(int userId);

}
