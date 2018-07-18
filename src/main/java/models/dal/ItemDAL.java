package models.dal;

import java.io.InputStream;

import models.constant.ItemType;

public class ItemDAL {

	public Integer itemId;
	public String itemName;
	public InputStream itemImage;
	public String imageFormat;
	public ItemType itemType;
	public String description;
	public Integer minCharacterLevel;
	public Integer attackPoints;
	public Integer defencePoints;

}
