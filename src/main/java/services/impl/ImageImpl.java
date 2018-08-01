package services.impl;

import models.business.ProfileImage;
import models.business.Player;
import models.constant.Error;
import models.constant.ImageType;
import models.constant.Success;
import models.dal.ImageDAL;
import models.dal.UserDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;
import services.ICache;
import services.IHigherService;
import services.IImage;

public class ImageImpl implements IImage {

	private IHigherService _higherService;
	private ICache _cache;

	public ImageImpl(HigherService higherService) {
		_higherService = higherService;
		_cache = CacheImpl.getInstance();
	}

	/**
	 * If user A has no his own image, will be got default image 1. Method gets
	 * image from the cache. If image in a cache doesn't exists, method downloads it
	 * from a database ant puts to cache.
	 */
	@Override
	public ObjectDTO<ProfileImage> getUserAImage(int userAId) {
		return getUserImage(userAId, true);
	}

	/**
	 * If user B has no his own image, will be got default image 2. Method gets
	 * image from the cache. If image in a cache doesn't exists, method downloads it
	 * from a database ant puts to cache.
	 */
	@Override
	public ObjectDTO<ProfileImage> getUserBImage(int userBId) {
		return getUserImage(userBId, false);
	}

	/**
	 * Method add's new user image to database image table, updates user info in a
	 * database user table, updates user info in a cache.
	 */
	@Override
	public ObjectDTO<Integer> addImage(int userId, String imageName, ImageType imageType, byte[] image) {
		ProfileImage profileImage = new ProfileImage();
		profileImage.userId = userId;
		profileImage.imageName = imageName;
		profileImage.imageType = imageType;
		profileImage.image = image;

		ObjectDTO<Integer> createImageDTO = _higherService.createNewImage(profileImage);
		if (createImageDTO.success) {
			ObjectDTO<UserDAL> userUpdateDTO = _higherService.updateUserImageId(userId, createImageDTO.transferData);
			if (userUpdateDTO.success) {
				updateUserInCache(userUpdateDTO.transferData);
				return createImageDTO;
			} else {
				_higherService.deleteImage(createImageDTO.transferData);
				ObjectDTO<Integer> unsuccessfulImageDTO = new ObjectDTO<>();
				unsuccessfulImageDTO.message = userUpdateDTO.message;
				return unsuccessfulImageDTO;
			}

		} else {
			return createImageDTO;
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

		ProfileImage profileImage = new ProfileImage();
		profileImage.imageId = imageIdDTO.transferData;
		profileImage.userId = userId;
		profileImage.imageName = imageName;
		profileImage.imageType = imageType;
		profileImage.image = image;

		editDTO = _higherService.updateImage(profileImage);
		if (editDTO.success) {
			_cache.removeImage(profileImage.imageId);
		}
		return editDTO;
	}

	/**
	 * Changes default image (imageId can be only 1 or 2). Remove old default image
	 * from cache if exists. Method should be used by administrators only.
	 */
	@Override
	public DTO editDefaultImage(int imageId, String imageName, ImageType imageType, byte[] image) {
		DTO editDTO = new DTO();

		if (imageId < 1 || imageId > 2) {
			editDTO.message = Error.WRONG_DEFAULT_IMAGE_ID.getMessage();
			return editDTO;
		}

		ProfileImage profileImage = new ProfileImage();
		profileImage.imageId = imageId;
		profileImage.imageName = imageName;
		profileImage.imageType = imageType;
		profileImage.image = image;

		editDTO = _higherService.updateImage(profileImage);
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

		deleteDTO = _higherService.deleteImage(imageIdDTO.transferData);
		if (deleteDTO.success) {
			_cache.removeImage(imageIdDTO.transferData);
		}
		return deleteDTO;
	}

	private ObjectDTO<ProfileImage> getUserImage(int userId, boolean isUserA) {
		ObjectDTO<ProfileImage> profileImageDTO = new ObjectDTO<>();
		Player player = _cache.getPlayer(userId);

		int imageId;
		if (player != null && player.user.imageId != null) {
			imageId = player.user.imageId;
		} else {
			imageId = isUserA ? 1 : 2;
		}

		ImageDAL imageDAL = _cache.getImage(imageId);

		if (imageDAL != null) {
			profileImageDTO.transferData = imageDALtoProfileImage(imageDAL);
			profileImageDTO.message = Success.IMAGE_IS_TAKEN_FROM_CACHE.getMessage();
			profileImageDTO.success = true;
			return profileImageDTO;
		}

		ObjectDTO<ImageDAL> imageDTO = _higherService.getImage(imageId);

		if (imageDTO.success) {
			profileImageDTO.transferData = imageDALtoProfileImage(imageDTO.transferData);
			_cache.addImage(imageDTO.transferData);
		}

		profileImageDTO.message = imageDTO.message;
		profileImageDTO.success = imageDTO.success;
		return profileImageDTO;
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
		return _higherService.getImageId(userId);
	}

	private ProfileImage imageDALtoProfileImage(ImageDAL imageDAL) {
		ProfileImage profileImage = new ProfileImage();
		if (imageDAL != null) {
			profileImage.imageId = imageDAL.imageId != null ? imageDAL.imageId : 0;
			profileImage.userId = imageDAL.userId != null ? imageDAL.userId : 0;
			profileImage.image = imageDAL.image;
			if (imageDAL.imageName != null && imageDAL.imageName.contains(".")) {
				profileImage.imageName = imageDAL.imageName.substring(0, imageDAL.imageName.lastIndexOf("."));
				profileImage.imageType = ImageType
						.getByImageExtension(imageDAL.imageName.substring(imageDAL.imageName.lastIndexOf(".")));
			} else {
				profileImage.imageName = imageDAL.imageName;
			}
		}
		return profileImage;
	}

}
