package services.impl;

import java.util.HashMap;
import java.util.Map;

import models.constant.Error;
import models.constant.ImageType;
import models.constant.ItemType;
import models.constant.Success;
import models.dal.ItemDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.ICache;
import services.IItem;
import services.ILog;

public class ItemImpl implements IItem {

	private ICRUD _crud;
	private ICache _cache;
	private ILog _log;

	public ItemImpl(CRUDImpl crud) {
		_crud = crud;
		_cache = CacheImpl.getInstance();
		_log = LogImpl.getInstance();
	}

	/**
	 * Method gets item from the cache. If there is no item in a cache, method
	 * downloads it from a database ant puts it to cache. In case of error, if there
	 * is no item with such Id, method will try get and return item No 1.
	 */
	@Override
	public ObjectDTO<ItemDAL> getItem(int itemId) {
		ObjectDTO<ItemDAL> itemDTO = new ObjectDTO<>();
		if (_cache.getItem(itemId) != null) {
			itemDTO.transferData = _cache.getItem(itemId);
			itemDTO.message = Success.ITEM_IS_TAKEN_FROM_CACHE.getMessage();
			itemDTO.success = true;
			return itemDTO;
		}
		itemDTO = downloadItemFromDatabase(itemId);
		if (itemDTO.success || itemId == 1) {
			if (itemDTO.success) {
				_cache.addItem(itemDTO.transferData);
			}
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
	public Map<ItemType, ItemDAL> getUserItems(int userId) {
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

		ItemDAL attackItemDAL = getUserItem(attackItemId);
		ItemDAL defenceItemDAL = getUserItem(defenceItemId);

		Map<ItemType, ItemDAL> userItems = new HashMap<>();
		userItems.put(ItemType.ATTACK, attackItemDAL);
		userItems.put(ItemType.DEFENCE, defenceItemDAL);
		return userItems;
	}

	@Override
	public ObjectDTO<Integer> createNewItem(String itemName, byte[] itemImage, ImageType imageType, ItemType itemType,
			String description, int minCharacterLevel, int attackPoints, int defencePoints) {

		if (minCharacterLevel < 1) {
			return createInputParameterErrorDTO("createNewItem");
		}

		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemName = itemName;
		itemDAL.itemImage = itemImage;
		itemDAL.imageFormat = imageType.getImageExtension();
		itemDAL.itemType = itemType;
		itemDAL.description = !description.equals("") ? description : null;
		itemDAL.minCharacterLevel = minCharacterLevel;
		itemDAL.attackPoints = attackPoints;
		itemDAL.defencePoints = defencePoints;
		return _crud.create(itemDAL);
	}

	@Override
	public DTO editItem(int itemId, String itemName, byte[] itemImage, ImageType imageType, ItemType itemType,
			String description, int minCharacterLevel, int attackPoints, int defencePoints) {

		if (minCharacterLevel < 1) {
			return createInputParameterErrorDTO("editItem");
		}

		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemId = itemId;
		itemDAL.itemName = itemName;
		itemDAL.itemImage = itemImage;
		itemDAL.imageFormat = imageType.getImageExtension();
		itemDAL.itemType = itemType;
		itemDAL.description = !description.equals("") ? description : null;
		itemDAL.minCharacterLevel = minCharacterLevel;
		itemDAL.attackPoints = attackPoints;
		itemDAL.defencePoints = defencePoints;

		DTO updateDTO = _crud.update(itemDAL);
		if (updateDTO.success) {
			_cache.removeItem(itemId);
		}

		return updateDTO;
	}

	@Override
	public DTO deleteItem(int itemId) {
		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemId = itemId;
		return _crud.delete(itemDAL);
	}

	private ObjectDTO<ItemDAL> downloadItemFromDatabase(int itemId) {
		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemId = itemId;
		ListDTO<ItemDAL> listDTO = _crud.read(itemDAL);
		ObjectDTO<ItemDAL> itemDTO = new ObjectDTO<>();
		if (listDTO.success && !listDTO.transferDataList.isEmpty()) {
			itemDTO.transferData = listDTO.transferDataList.get(0);
			itemDTO.message = listDTO.message;
			itemDTO.success = true;
			return itemDTO;
		} else {
			_log.writeWarningMessage(Error.ITEM_WASNT_DOWNLOADED_FROM_DB.getMessage(), true, "Item No " + itemId,
					"Class: CacheImpl, method: private boolean downloadItemFromDatabase(int itemId).",
					"crud read message: " + listDTO.message);
			itemDTO.message = listDTO.message;
			return itemDTO;
		}
	}

	private ItemDAL getUserItem(int itemId) {
		if (_cache.getItem(itemId) != null) {
			return _cache.getItem(itemId);
		} else {
			return getItem(itemId).transferData;
		}
	}

	private ObjectDTO<Integer> createInputParameterErrorDTO(String methodName) {
		ObjectDTO<Integer> objectDTO = new ObjectDTO<>();
		objectDTO.message = Error.ITEM_CREATE_WRONG_MIN_CHARACTER_LEVEL.getMessage();
		_log.writeWarningMessage(Error.ITEM_CREATE_WRONG_MIN_CHARACTER_LEVEL.getMessage(), true,
				"Class: ItemImpl, Method: " + methodName + "(input parameters)");
		return objectDTO;
	}

}
