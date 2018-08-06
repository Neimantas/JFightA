package services;

import java.util.Map;

import models.business.Item;
import models.constant.ItemType;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IItem {

	ObjectDTO<Item> getItem(int itemId);
	Map<ItemType, Item> getUserItems(int userId);
	ObjectDTO<Integer> createNewItem(Item item);
	DTO editItem(Item item);
	DTO deleteItem(int itemId);
	
}
