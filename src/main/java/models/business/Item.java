package models.business;

import models.constant.ImageType;
import models.constant.ItemType;

public class Item {
	
	public int itemId;
	public String itemName;
	public byte[] itemImage;
	public ImageType imageFormat;
	public ItemType itemType;
	public String description;
	public int minCharacterLevel;
	public int attackPoints;
	public int defencePoints;

}
