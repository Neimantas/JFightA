package services.impl;

import java.util.HashMap;
import java.util.Map;

import models.business.Player;

import models.constant.ItemType;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dal.UserDAL;
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
	 * If user A has no his own image, will be downloaded default image 1.
	 */
	@Override
	public ObjectDTO<ImageDAL> getUserAImage(int userAId) {
		return getUserImage(userAId, true);
	}

	/**
	 * If user B has no his own image, will be downloaded default image 2.
	 */
	@Override
	public ObjectDTO<ImageDAL> getUserBImage(int userBId) {
		System.out.println("====================================================");
		return getUserImage(userBId, false);
	}

	@Override
	public ObjectDTO<ItemDAL> getItem(int itemId) {
		ObjectDTO<ItemDAL> itemDTO = new ObjectDTO<>();
		if (_cache.getItem(itemId) != null) {
			itemDTO.transferData = _cache.getItem(itemId);
			itemDTO.message = "Item is taken from cache.";
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

	private ObjectDTO<ImageDAL> getUserImage(int userId, boolean isUserA) {
		ImageDAL imageDAL = new ImageDAL();
		ObjectDTO<ImageDAL> imageDTO = new ObjectDTO<>();
		Player player = _cache.getPlayer(userId);
		if (player != null && player.user.imageId != null) {
			imageDAL.imageId = player.user.imageId;
			ListDTO<ImageDAL> imageListDTO = _crud.read(imageDAL);
			if (imageListDTO.success == true && !imageListDTO.transferDataList.isEmpty()) {
				imageListDTOtoImageDTO(imageDTO, imageListDTO);
				return imageDTO;
			}
		} else {
			imageDAL.imageId = isUserA ? 1 : 2;
			ListDTO<ImageDAL> imageListDTO = _crud.read(imageDAL);
			if (imageListDTO.success == true && !imageListDTO.transferDataList.isEmpty()) {
				imageListDTOtoImageDTO(imageDTO, imageListDTO);
				return imageDTO;
			} else {
				imageDTO.message = imageListDTO.message;
			}
		}
		return imageDTO;
	}

	private void imageListDTOtoImageDTO(ObjectDTO<ImageDAL> imageDTO, ListDTO<ImageDAL> imageListDTO) {
		imageDTO.transferData = imageListDTO.transferDataList.get(0);
		imageDTO.message = imageListDTO.message;
		imageDTO.success = true;
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
			_log.writeWarningMessage("Item No " + itemId + " wasn't downloaded from the database.", true,
					"Class: CacheImpl, method: private boolean downloadItem(int itemId).");
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

	@Override
	public ObjectDTO<Integer> addImage(int userId, String imageName, byte[] image) {
		ImageDAL imageDAL = new ImageDAL();
		imageDAL.userId = userId;
		imageDAL.imageName = imageName;
		imageDAL.image = image;
		ObjectDTO<Integer> imageDTO = _crud.create(imageDAL);
		if (imageDTO.success) {
			UserDAL userDAL = new UserDAL();
			userDAL.userId = userId;
			ListDTO<UserDAL> userListDTO = _crud.read(userDAL);
			if (userListDTO.success && !userListDTO.transferDataList.isEmpty()) {
				userDAL = userListDTO.transferDataList.get(0);
				userDAL.imageId = imageDTO.transferData;
				DTO dto = _crud.update(userDAL);
				if (dto.success) {
					return imageDTO;
				}
			}
		} else {
			return imageDTO;
		}
		
		return null;
	}

	@Override
	public DTO editImage(int userId, String imageName, byte[] image) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DTO editDefaultImage(int imageId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DTO deleteImage(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
