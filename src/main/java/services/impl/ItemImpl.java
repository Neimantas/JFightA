package services.impl;

import java.util.Map;

import models.business.Player;
import models.business.User;
import models.constant.ItemType;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dal.UserDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.ICache;
import services.IItem;

public class ItemImpl implements IItem {

	private ICRUD _crud;
	private ICache _cache;

	public ItemImpl() {
		_crud = CRUDImpl.getInstance();
		_cache = CacheImpl.getInstance();
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
	public ObjectDTO<ItemDAL> getItem(int itemId) {
		// Integer itemId = _cache.getPlayer(userName).character.
		return null;
	}

	@Override
	public Map<ItemType, Integer> getItemPoints(int userId) {
		// TODO Auto-generated method stub
		return null;
	}

	private ObjectDTO<ImageDAL> getUserImage(int userId, boolean userA) {
		ImageDAL imageDAL = new ImageDAL();
		ObjectDTO<ImageDAL> imageDTO = new ObjectDTO<>();
		User user = _cache.getPlayer(userId).user;
		if (user != null) {
			if (user.imageId != null) {
				imageDAL.imageId = user.imageId;
				ListDTO<ImageDAL> imageListDTO = _crud.read(imageDAL);
				if (imageListDTO.success == true && !imageListDTO.transferDataList.isEmpty()) {
					imageListDTOtoImageDTO(imageDTO, imageListDTO);
					return imageDTO;
				}
			}
		} else {
			imageDAL.imageId = userA ? 1 : 2;
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

	public void imageListDTOtoImageDTO(ObjectDTO<ImageDAL> imageDTO, ListDTO<ImageDAL> imageListDTO) {
		imageDTO.transferData = imageListDTO.transferDataList.get(0);
		imageDTO.message = imageListDTO.message;
		imageDTO.success = true;
	}

}
