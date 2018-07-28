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
		return getUserImage(userBId, false);
	}

	@Override
	public ObjectDTO<Integer> addImage(int userId, String imageName, byte[] image) {
		ImageDAL imageDAL = new ImageDAL();
		imageDAL.userId = userId;
		imageDAL.imageName = imageName;
		imageDAL.image = image;

		ObjectDTO<Integer> imageDTO = _crud.create(imageDAL);
		if (imageDTO.success) {
			DTO userUpdateDTO = updateUser(userId, imageDTO.transferData);

			if (userUpdateDTO.success) {
				return imageDTO;
			} else {
				ImageDAL deleteImageDAL = new ImageDAL();
				deleteImageDAL.imageId = imageDTO.transferData;
				_crud.delete(deleteImageDAL);
				ObjectDTO<Integer> unsuccessfulImageDTO = new ObjectDTO<>();
				unsuccessfulImageDTO.message = userUpdateDTO.message;
				return unsuccessfulImageDTO;
			}

		} else {
			return imageDTO;
		}
	}

	@Override
	public DTO editImage(int userId, String imageName, byte[] image) {
		DTO editDTO = new DTO();
		ObjectDTO<Integer> imageIdDTO = getImageId(userId);

		if (!imageIdDTO.success) {
			editDTO.message = imageIdDTO.message;
			return editDTO;
		}

		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageIdDTO.transferData;
		imageDAL.userId = userId;
		imageDAL.imageName = imageName;
		imageDAL.image = image;

		editDTO = _crud.update(imageDAL);
		if (editDTO.success) {
			_cache.removeImage(imageDAL.imageId);
		}
		return editDTO;
	}

	@Override
	public DTO editDefaultImage(int imageId, String imageName, byte[] image) {
		DTO editDTO = new DTO();

		if (imageId < 1 || imageId > 2) {
			editDTO.message = "Wrong default image Id.";
			return editDTO;
		}

		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageId;
		imageDAL.imageName = imageName;
		imageDAL.image = image;

		editDTO = _crud.update(imageDAL);
		if (editDTO.success) {
			_cache.removeImage(imageId);
		}
		return editDTO;
	}

	@Override
	public DTO deleteImage(int userId) {
		DTO deleteDTO = new DTO();
		ObjectDTO<Integer> imageIdDTO = getImageId(userId);

		if (!imageIdDTO.success) {
			deleteDTO.message = imageIdDTO.message;
			return deleteDTO;
		}

		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageIdDTO.transferData;

		deleteDTO = _crud.delete(imageDAL);
		if (deleteDTO.success) {
			_cache.removeImage(imageDAL.imageId);
		}
		return deleteDTO;
	}

	@Override
	public ObjectDTO<ItemDAL> getItem(int itemId) {
		ObjectDTO<ItemDAL> itemDTO = new ObjectDTO<>();
		if (_cache.getItem(itemId) != null) {
			itemDTO.transferData = _cache.getItem(itemId);
			itemDTO.message = "Item is taken from the cache.";
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
		ObjectDTO<ImageDAL> imageDTO = new ObjectDTO<>();
		Player player = _cache.getPlayer(userId);

		int imageId;
		if (player != null && player.user.imageId != null) {
			imageId = player.user.imageId;
		} else {
			imageId = isUserA ? 1 : 2;
		}

		ImageDAL imageDAL = _cache.getImage(imageId);

		if (imageDAL != null) {
			imageDTO.transferData = imageDAL;
			imageDTO.message = "Image is taken from the cache.";
			imageDTO.success = true;
			return imageDTO;
		}

		imageDTO = downloadImageFromDatabase(imageId);

		if (imageDTO.success) {
			_cache.addImage(imageDTO.transferData);
		}

		return imageDTO;
	}

	private ObjectDTO<ImageDAL> downloadImageFromDatabase(int imageId) {
		ObjectDTO<ImageDAL> imageDTO = new ObjectDTO<>();
		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageId;
		ListDTO<ImageDAL> imageListDTO = _crud.read(imageDAL);
		if (imageListDTO.success == true && !imageListDTO.transferDataList.isEmpty()) {
			imageListDTOtoImageDTO(imageDTO, imageListDTO);
			return imageDTO;
		} else {
			imageDTO.message = imageListDTO.message;
		}
		return imageDTO;
	}

	private void imageListDTOtoImageDTO(ObjectDTO<ImageDAL> imageDTO, ListDTO<ImageDAL> imageListDTO) {
		imageDTO.transferData = imageListDTO.transferDataList.get(0);
		imageDTO.message = imageListDTO.message;
		imageDTO.success = imageListDTO.success;
	}

	private DTO updateUser(int userId, int imageId) {
		UserDAL userDAL = new UserDAL();
		userDAL.userId = userId;
		ListDTO<UserDAL> userListDTO = _crud.read(userDAL);
		if (userListDTO.success && !userListDTO.transferDataList.isEmpty()) {
			userDAL = userListDTO.transferDataList.get(0);
			userDAL.imageId = imageId;
			DTO dto = _crud.update(userDAL);
			if (dto.success) {
				updateUserInCache(userDAL);
			}
			return dto;
		} else {
			DTO dto = new DTO();
			dto.message = userListDTO.message;
			return dto;
		}
	}

	private void updateUserInCache(UserDAL userDAL) {
		Player player = _cache.getPlayer(userDAL.userId);
		if (player != null) {
			player.user.imageId = userDAL.imageId;
		}
	}

	private ObjectDTO<Integer> getImageId(int userId) {
		ObjectDTO<Integer> imageIdDTO = new ObjectDTO<>();

		Player player = _cache.getPlayer(userId);

		if (player != null) {
			if (player.user.imageId != null) {
				imageIdDTO.transferData = player.user.imageId;
				imageIdDTO.message = "User has an image.";
				imageIdDTO.success = true;
				return imageIdDTO;
			} else {
				imageIdDTO.message = "User has no image.";
				return imageIdDTO;
			}
		}

		return getImageIdFromDatabase(userId);
	}

	private ObjectDTO<Integer> getImageIdFromDatabase(int userId) {
		ObjectDTO<Integer> imageIdDTO = new ObjectDTO<>();
		UserDAL userDAL = new UserDAL();
		userDAL.userId = userId;
		ListDTO<UserDAL> listDTO = _crud.read(userDAL);
		if (listDTO.success) {
			if (!listDTO.transferDataList.isEmpty() && listDTO.transferDataList.get(0).imageId != null) {
				imageIdDTO.transferData = listDTO.transferDataList.get(0).imageId;
				imageIdDTO.message = "User has an image.";
				imageIdDTO.success = true;
				return imageIdDTO;
			}
			imageIdDTO.message = "User has no image.";
			return imageIdDTO;
		}
		imageIdDTO.message = listDTO.message;
		return imageIdDTO;
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

}
