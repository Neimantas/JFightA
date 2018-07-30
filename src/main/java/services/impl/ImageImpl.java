package services.impl;

import models.business.Player;
import models.constant.Error;
import models.constant.ImageType;
import models.constant.Success;
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
	 * If user A has no his own image, will be got default image 1.
	 * Method gets image from the cache. If image in a cache doesn't exists,
	 * method downloads it from a database ant puts to cache.
	 */
	@Override
	public ObjectDTO<ImageDAL> getUserAImage(int userAId) {
		return getUserImage(userAId, true);
	}

	/**
	 * If user B has no his own image, will be got default image 2.
	 * Method gets image from the cache. If image in a cache doesn't exists,
	 * method downloads it from a database ant puts to cache.
	 */
	@Override
	public ObjectDTO<ImageDAL> getUserBImage(int userBId) {
		return getUserImage(userBId, false);
	}

	/**
	 * Method add's new user image to database image table, updates user info in a
	 * database user table, updates user info in a cache.
	 */
	@Override
	public ObjectDTO<Integer> addImage(int userId, String imageName, ImageType imageType, byte[] image) {
		ImageDAL imageDAL = new ImageDAL();
		imageDAL.userId = userId;
		imageDAL.imageName = imageName + imageType.getImageExtension();
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

	/**
	 * Changes user image in a database image table. Removes old user image from
	 * cache if exists.
	 */
	@Override
	public DTO editImage(int userId, String imageName, ImageType imageType, byte[] image) {
		DTO editDTO = new DTO();
		ObjectDTO<Integer> imageIdDTO = getImageId(userId);

		if (!imageIdDTO.success) {
			editDTO.message = imageIdDTO.message;
			return editDTO;
		}

		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageIdDTO.transferData;
		imageDAL.userId = userId;
		imageDAL.imageName = imageName + imageType.getImageExtension();
		imageDAL.image = image;

		editDTO = _crud.update(imageDAL);
		if (editDTO.success) {
			_cache.removeImage(imageDAL.imageId);
		}
		return editDTO;
	}

	/**
	 * Changes default image (imageId can be only 1 or 2). Remove old default image
	 * from cache if exists.
	 * Method should be used by administrators only.
	 */
	@Override
	public DTO editDefaultImage(int imageId, String imageName, ImageType imageType, byte[] image) {
		DTO editDTO = new DTO();

		if (imageId < 1 || imageId > 2) {
			editDTO.message = Error.WRONG_DEFAULT_IMAGE_ID.getMessage();
			return editDTO;
		}

		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageId;
		imageDAL.imageName = imageName + imageType.getImageExtension();
		imageDAL.image = image;

		editDTO = _crud.update(imageDAL);
		if (editDTO.success) {
			_cache.removeImage(imageId);
		}
		return editDTO;
	}

	/**
	 * Deletes user image from a database and from a cache if exists.
	 */
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
			imageDTO.message = Success.IMAGE_IS_TAKEN_FROM_CACHE.getMessage();
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
				imageIdDTO.message = Success.USER_HAS_AN_IMAGE.getMessage();
				imageIdDTO.success = true;
				return imageIdDTO;
			} else {
				imageIdDTO.message = Error.USER_HAS_NO_IMAGE.getMessage();
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
				imageIdDTO.message = Success.USER_HAS_AN_IMAGE.getMessage();
				imageIdDTO.success = true;
				return imageIdDTO;
			}
			imageIdDTO.message = Error.USER_HAS_NO_IMAGE.getMessage();
			return imageIdDTO;
		}
		imageIdDTO.message = listDTO.message;
		return imageIdDTO;
	}

}
