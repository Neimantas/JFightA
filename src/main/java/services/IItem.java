package services;

import java.util.Map;

import models.constant.ItemType;
import models.dal.ItemDAL;
import models.dto.ObjectDTO;

public interface IItem {

	ObjectDTO<ItemDAL> getItem(int itemId);
	Map<ItemType, ItemDAL> getUserItems(int userId);
	
}
