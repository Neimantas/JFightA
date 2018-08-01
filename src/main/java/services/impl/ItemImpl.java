package services.impl;

import java.util.HashMap;
import java.util.Map;

import models.business.Item;
import models.constant.Error;
import models.constant.ImageType;
import models.constant.ItemType;
import models.constant.Success;
import models.dal.ItemDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;
import services.ICache;
import services.IHigherService;
import services.IItem;
import services.ILog;

public class ItemImpl implements IItem {

	private IHigherService _higherService;
	private ICache _cache;
	private ILog _log;

	public ItemImpl(HigherService higherService) {
		_higherService = higherService;
		_cache = CacheImpl.getInstance();
		_log = LogImpl.getInstance();
	}

	/**
	 * Method gets item from the cache. If there is no item in a cache, method
	 * downloads it from a database ant puts it to cache. In case of error, if there
	 * is no item with such Id, method will try get and return item No 1.
	 */
	@Override
	public ObjectDTO<Item> getItem(int itemId) {
		ObjectDTO<Item> itemDTO = new ObjectDTO<>();
		if (_cache.getItem(itemId) != null) {
			itemDTO.transferData = itemDALtoItem(_cache.getItem(itemId));
			itemDTO.message = Success.ITEM_IS_TAKEN_FROM_CACHE.getMessage();
			itemDTO.success = true;
			return itemDTO;
		}
		ObjectDTO<ItemDAL> itemDALdto = _higherService.getItem(itemId);
		if (itemDALdto.success || itemId == 1) {
			if (itemDALdto.success) {
				_cache.addItem(itemDALdto.transferData);
				itemDTO.transferData = itemDALtoItem(itemDALdto.transferData);
			}
			itemDTO.message = itemDALdto.message;
			itemDTO.success = itemDALdto.success;
			return itemDTO;
		} else {
			return getItem(1);
		}
	}

	/**
	 * Returns user items. In case of error will try to return ATTACK item No 1 and
	 * DEFENCE item No 2.
	 */
	@Override
	public Map<ItemType, Item> getUserItems(int userId) {
		int attackItemId = 0;
		int defenceItemId = 0;
		if (_cache.getPlayer(userId) != null) {
			attackItemId = (_cache.getPlayer(userId).characterInfo.attackItemId != null
					&& _cache.getPlayer(userId).characterInfo.attackItemId != 0)
							? _cache.getPlayer(userId).characterInfo.attackItemId
							: 1;
			defenceItemId = (_cache.getPlayer(userId).characterInfo.defenceItemId != null
					&& _cache.getPlayer(userId).characterInfo.defenceItemId != 0)
							? _cache.getPlayer(userId).characterInfo.defenceItemId
							: 2;
		} else {
			attackItemId = 1;
			defenceItemId = 2;
		}

		Item attackItem = getUserItem(attackItemId);
		Item defenceItem = getUserItem(defenceItemId);

		Map<ItemType, Item> userItems = new HashMap<>();
		userItems.put(ItemType.ATTACK, attackItem);
		userItems.put(ItemType.DEFENCE, defenceItem);
		return userItems;
	}

	@Override
	public ObjectDTO<Integer> createNewItem(String itemName, byte[] itemImage, ImageType imageType, ItemType itemType,
			String description, int minCharacterLevel, int attackPoints, int defencePoints) {

		if (itemName == null || itemName.equals("")) {
			return createInputParameterErrorDTO(Error.ITEM_EMPTY_NAME, "createNewItem");
		}
		if (minCharacterLevel < 1) {
			return createInputParameterErrorDTO(Error.ITEM_WRONG_MIN_CHARACTER_LEVEL, "createNewItem");
		}

		Item item = new Item();
		item.itemName = itemName;
		item.itemImage = itemImage;
		item.imageFormat = imageType;
		item.itemType = itemType;
		item.description = description;
		item.minCharacterLevel = minCharacterLevel;
		item.attackPoints = attackPoints;
		item.defencePoints = defencePoints;
		return _higherService.createNewItem(item);
	}

	@Override
	public DTO editItem(int itemId, String itemName, byte[] itemImage, ImageType imageType, ItemType itemType,
			String description, int minCharacterLevel, int attackPoints, int defencePoints) {

		if (itemId < 1) {
			return createInputParameterErrorDTO(Error.ITEM_WRONG_ID, "editItem");
		}
		if (itemName == null || itemName.equals("")) {
			return createInputParameterErrorDTO(Error.ITEM_EMPTY_NAME, "editItem");
		}
		if (minCharacterLevel < 1) {
			return createInputParameterErrorDTO(Error.ITEM_WRONG_MIN_CHARACTER_LEVEL, "editItem");
		}

		Item item = new Item();
		item.itemId = itemId;
		item.itemName = itemName;
		item.itemImage = itemImage;
		item.imageFormat = imageType;
		item.itemType = itemType;
		item.description = description;
		item.minCharacterLevel = minCharacterLevel;
		item.attackPoints = attackPoints;
		item.defencePoints = defencePoints;

		DTO updateDTO = _higherService.editItem(item);
		if (updateDTO.success) {
			_cache.removeItem(itemId);
		}

		return updateDTO;
	}

	@Override
	public DTO deleteItem(int itemId) {
		if (itemId < 1) {
			return createInputParameterErrorDTO(Error.ITEM_WRONG_ID, "deleteItem");
		}

		DTO deleteDTO = _higherService.deleteItem(itemId);
		if (deleteDTO.success) {
			_cache.removeItem(itemId);
		}

		return deleteDTO;
	}

	private Item getUserItem(int itemId) {
		if (_cache.getItem(itemId) != null) {
			return itemDALtoItem(_cache.getItem(itemId));
		} else {
			return getItem(itemId).transferData;
		}
	}

	private ObjectDTO<Integer> createInputParameterErrorDTO(Error error, String methodName) {
		ObjectDTO<Integer> objectDTO = new ObjectDTO<>();
		objectDTO.message = error.getMessage();
		_log.writeWarningMessage(error.getMessage(), true,
				"Class: ItemImpl, Method: " + methodName + "(input parameters)");
		return objectDTO;
	}

	private Item itemDALtoItem(ItemDAL itemDAL) {
		Item item = new Item();
		if (itemDAL != null) {
			item.itemId = itemDAL.itemId;
			item.itemName = itemDAL.itemName;
			item.itemImage = itemDAL.itemImage;
			item.imageFormat = ImageType.getByImageExtension(itemDAL.imageFormat);
			item.itemType = itemDAL.itemType;
			item.description = (itemDAL.description != null && !itemDAL.description.equals("")) ? itemDAL.description
					: "";
			item.minCharacterLevel = itemDAL.minCharacterLevel != null ? itemDAL.minCharacterLevel : 0;
			item.attackPoints = itemDAL.attackPoints != null ? itemDAL.attackPoints : 0;
			item.defencePoints = itemDAL.defencePoints != null ? itemDAL.defencePoints : 0;
		}
		return item;
	}

}
