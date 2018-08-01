package services;

import java.util.Map;

import models.business.Item;
import models.constant.ImageType;
import models.constant.ItemType;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IItem {

	ObjectDTO<Item> getItem(int itemId);
	Map<ItemType, Item> getUserItems(int userId);
	ObjectDTO<Integer> createNewItem(String itemName, byte[] itemImage, ImageType imageType, ItemType itemType,
			String description, int minCharacterLevel, int attackPoints, int defencePoints);
	DTO editItem(int itemId, String itemName, byte[] itemImage, ImageType imageType, ItemType itemType,
			String description, int minCharacterLevel, int attackPoints, int defencePoints);
	DTO deleteItem(int itemId);
	
}
