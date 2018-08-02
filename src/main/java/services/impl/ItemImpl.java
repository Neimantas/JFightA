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

	/**
	 * Creates new item. Item Id must be zero. Returns DTO with created item Id
	 * inside.
	 */
	@Override
	public ObjectDTO<Integer> createNewItem(Item item) {
		DTO correctInputDTO = checkIfInputParametersIsCorrect(item, false);
		if (!correctInputDTO.success) {
			ObjectDTO<Integer> errorDTO = new ObjectDTO<>();
			errorDTO.message = correctInputDTO.message;
			return errorDTO;
		}

		return _higherService.createNewItem(itemToItemDAL(item));
	}

	/**
	 * Method overwrites all item fields. All input item fields must be setted.
	 */
	@Override
	public DTO editItem(Item item) {
		DTO correctInputDTO = checkIfInputParametersIsCorrect(item, true);
		if (!correctInputDTO.success) {
			return correctInputDTO;
		}

		DTO updateDTO = _higherService.editItem(itemToItemDAL(item));
		if (updateDTO.success) {
			_cache.removeItem(item.itemId);
		}

		return updateDTO;
	}

	@Override
	public DTO deleteItem(int itemId) {
		if (itemId < 1) {
			return createInputParameterErrorDTO(Error.ITEM_WRONG_ID, "deleteItem");
		}

		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemId = itemId;
		DTO deleteDTO = _higherService.deleteItem(itemDAL);
		
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

	private Item itemDALtoItem(ItemDAL itemDAL) {
		Item item = new Item();
		if (itemDAL != null) {
			item.itemId = itemDAL.itemId;
			item.itemName = itemDAL.itemName;
			item.itemImage = itemDAL.itemImage;
			item.imageFormat = ImageType.getByImageExtension(itemDAL.imageFormat);
			item.itemType = itemDAL.itemType;
			item.description = (itemDAL.description != null && !itemDAL.description.equals("")) ? itemDAL.description : "";
			item.minCharacterLevel = itemDAL.minCharacterLevel != null ? itemDAL.minCharacterLevel : 0;
			item.attackPoints = itemDAL.attackPoints != null ? itemDAL.attackPoints : 0;
			item.defencePoints = itemDAL.defencePoints != null ? itemDAL.defencePoints : 0;
		}
		return item;
	}

	private ItemDAL itemToItemDAL(Item item) {
		ItemDAL itemDAL = new ItemDAL();
		if (item != null) {
			itemDAL.itemId = item.itemId != 0 ? item.itemId : null;
			itemDAL.itemName = (item.itemName != null && !item.itemName.equals("")) ? item.itemName : null;
			itemDAL.itemImage = item.itemImage;
			itemDAL.imageFormat = item.imageFormat.getImageExtension();
			itemDAL.itemType = item.itemType;
			itemDAL.description = (item.description != null && !item.description.equals("")) ? item.description : null;
			itemDAL.minCharacterLevel = item.minCharacterLevel != 0 ? item.minCharacterLevel : null;
			itemDAL.attackPoints = item.attackPoints != 0 ? item.attackPoints : null;
			itemDAL.defencePoints = item.defencePoints != 0 ? item.defencePoints : null;
		}
		return itemDAL;
	}

	private DTO checkIfInputParametersIsCorrect(Item item, boolean editMethod) {
		DTO dto = new DTO();
		String methodName = editMethod == true ? "editItem" : "createNewItem";
		if (editMethod && item.itemId < 1) {
			return createInputParameterErrorDTO(Error.ITEM_WRONG_ID, methodName);
		}
		if (item.itemName == null || item.itemName.equals("")) {
			return createInputParameterErrorDTO(Error.ITEM_EMPTY_NAME, methodName);
		}
		if (item.itemImage == null || item.itemImage.length == 0) {
			return createInputParameterErrorDTO(Error.IMAGE_IS_NOT_SETTED, methodName);
		}
		if (item.imageFormat == null) {
			return createInputParameterErrorDTO(Error.ITEM_WRONG_IMAGE_FORMAT, methodName);
		}
		if (item.itemType == null) {
			return createInputParameterErrorDTO(Error.ITEM_TYPE_IS_NOT_SETTED, methodName);
		}
		if (item.minCharacterLevel < 1) {
			return createInputParameterErrorDTO(Error.ITEM_WRONG_MIN_CHARACTER_LEVEL, methodName);
		}

		dto.message = Success.INPUT_PARAMETERS_IS_CORRECT.getMessage();
		dto.success = true;
		return dto;
	}

	private DTO createInputParameterErrorDTO(Error error, String methodName) {
		DTO errorDTO = new DTO();
		errorDTO.message = error.getMessage();
		_log.writeWarningMessage(error.getMessage(), "Class: ItemImpl, Method: " + methodName + "(input parameters)");
		return errorDTO;
	}

}
