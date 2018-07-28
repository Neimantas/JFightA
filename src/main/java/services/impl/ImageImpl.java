package services.impl;

import models.business.Player;

import models.dal.ImageDAL;
import models.dal.UserDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.ICache;
import services.IImage;

public class ImageImpl implements IImage {

	private ICRUD _crud;
	private ICache _cache;

	public ImageImpl(CRUDImpl crud) {
		_crud = crud;
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

}
