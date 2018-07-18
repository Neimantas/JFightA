package services.impl;

import models.dal.ImageDAL;
import models.dal.UserDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IItem;

public class ItemImpl implements IItem {

	private ICRUD _crud;

	public ItemImpl() {
		_crud = CRUDImpl.getInstance();
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

	private ObjectDTO<ImageDAL> getUserImage(int userId, boolean userA) {
		ObjectDTO<ImageDAL> imageDTO = new ObjectDTO<>();
		ListDTO<UserDAL> userListDTO = getUserById(userId);
		if (userListDTO.success) {
			ImageDAL imageDAL = new ImageDAL();
			if (!userListDTO.transferDataList.isEmpty() && userListDTO.transferDataList.get(0).imageId != null) {
				imageDAL.imageId = userListDTO.transferDataList.get(0).imageId;
				ListDTO<ImageDAL> imageListDTO = _crud.read(imageDAL);
				if (imageListDTO.success == true && !imageListDTO.transferDataList.isEmpty()) {
					imageListDTOtoImageDTO(imageDTO, imageListDTO);
					return imageDTO;
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
		}
		if (imageDTO.message == null) {
			imageDTO.message = userListDTO.message;
		}
		return imageDTO;
	}

	public void imageListDTOtoImageDTO(ObjectDTO<ImageDAL> imageDTO, ListDTO<ImageDAL> imageListDTO) {
		imageDTO.transferData = imageListDTO.transferDataList.get(0);
		imageDTO.message = imageListDTO.message;
		imageDTO.success = true;
	}

	private ListDTO<UserDAL> getUserById(int userId) {
		UserDAL userDAL = new UserDAL();
		userDAL.userId = userId;
		ListDTO<UserDAL> listDTO = _crud.read(userDAL);
		return listDTO;
	}

}
