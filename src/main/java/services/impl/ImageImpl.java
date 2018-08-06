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
import services.ILog;

public class ImageImpl implements IImage {

	private IHigherService _higherService;
	private ICache _cache;
	private ILog _log;

	public ImageImpl(HigherService higherService) {
		_higherService = higherService;
		_cache = CacheImpl.getInstance();
		_log = LogImpl.getInstance();
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
	 * database user table, updates user info in a cache. ProfileImage Id must be
	 * zero. Returns DTO with created image Id inside.
	 */
	@Override
	public ObjectDTO<Integer> addImage(ProfileImage profileImage) {

		DTO correctInputDTO = checkIfInputParametersIsCorrect(profileImage, "addImage");
		if (!correctInputDTO.success) {
			ObjectDTO<Integer> errorDTO = new ObjectDTO<>();
			errorDTO.message = correctInputDTO.message;
			return errorDTO;
		}

		ObjectDTO<Integer> createImageDTO = _higherService.createNewImage(profileImageToImageDAL(profileImage));
		if (createImageDTO.success) {
			ObjectDTO<UserDAL> userUpdateDTO = _higherService.updateUserImageId(profileImage.userId,
					createImageDTO.transferData);
			if (userUpdateDTO.success) {
				updateUserInCache(userUpdateDTO.transferData);
				return createImageDTO;
			} else {
				ImageDAL imageDAL = new ImageDAL();
				imageDAL.imageId = createImageDTO.transferData;
				DTO deleteDTO = _higherService.deleteImage(imageDAL);
				if (!deleteDTO.success) {
					_log.writeWarningMessage(deleteDTO.message, "ImageId: " + createImageDTO.transferData + ".",
							"Class: ImageImpl, method: addImage(ProfileImage profileImage).");
				}

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
	 * cache if exists. Method overwrites all image fields. All input profileImage
	 * fields except image Id must be setted.
	 */
	@Override
	public DTO editImage(ProfileImage profileImage) {
		DTO editDTO = new DTO();

		DTO correctInputDTO = checkIfInputParametersIsCorrect(profileImage, "editImage");
		if (!correctInputDTO.success) {
			return correctInputDTO;
		}

		ObjectDTO<Integer> imageIdDTO = getImageId(profileImage.userId);
		if (!imageIdDTO.success) {
			editDTO.message = imageIdDTO.message;
			return editDTO;
		}

		profileImage.imageId = imageIdDTO.transferData;

		editDTO = _higherService.updateImage(profileImageToImageDAL(profileImage));
		if (editDTO.success) {
			_cache.removeImage(profileImage.imageId);
		}
		return editDTO;
	}

	/**
	 * Changes default image. Removes old default image from cache if exists. Method
	 * overwrites all image fields. Image Id can be only 1 or 2. User Id must be
	 * zero. Method should be used by administrators only.
	 */
	@Override
	public DTO editDefaultImage(ProfileImage profileImage) {
		DTO editDTO = new DTO();

		DTO correctInputDTO = checkIfInputParametersIsCorrect(profileImage, "editDefaultImage");
		if (!correctInputDTO.success) {
			return correctInputDTO;
		}

		editDTO = _higherService.updateImage(profileImageToImageDAL(profileImage));
		if (editDTO.success) {
			_cache.removeImage(profileImage.imageId);
		}
		return editDTO;
	}

	/**
	 * Deletes user image from a database and from a cache if exists.
	 */
	@Override
	public DTO deleteImage(int userId) {
		if (userId < 1) {
			return createInputParameterErrorDTO(Error.IMAGE_WRONG_USER_ID, "deleteImage");
		}

		DTO deleteDTO = new DTO();
		ObjectDTO<Integer> imageIdDTO = getImageId(userId);

		if (!imageIdDTO.success) {
			deleteDTO.message = imageIdDTO.message;
			return deleteDTO;
		}

		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageIdDTO.transferData;
		deleteDTO = _higherService.deleteImage(imageDAL);
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

	private ImageDAL profileImageToImageDAL(ProfileImage profileImage) {
		ImageDAL imageDAL = new ImageDAL();
		if (profileImage != null) {
			imageDAL.imageId = profileImage.imageId != 0 ? profileImage.imageId : null;
			imageDAL.userId = profileImage.userId != 0 ? profileImage.userId : null;
			imageDAL.image = profileImage.image;
			imageDAL.imageName = (profileImage.imageName != null ? profileImage.imageName : "")
					+ (profileImage.imageType != null ? profileImage.imageType.getImageExtension() : "");
			if (imageDAL.imageName.equals("")) {
				imageDAL.imageName = null;
			}
		}
		return imageDAL;
	}

	private DTO checkIfInputParametersIsCorrect(ProfileImage profileImage, String methodName) {
		DTO dto = new DTO();
		if (methodName.equals("editDefaultImage") && (profileImage.imageId < 1 || profileImage.imageId > 2)) {
			return createInputParameterErrorDTO(Error.WRONG_DEFAULT_IMAGE_ID, methodName);
		}
		if (methodName.equals("editDefaultImage") && profileImage.userId != 0) {
			return createInputParameterErrorDTO(Error.DEFAULT_IMAGE_USER_ID_MUST_BE_ZERO, methodName);
		}
		if (!methodName.equals("editDefaultImage") && profileImage.userId < 0) {
			return createInputParameterErrorDTO(Error.IMAGE_WRONG_USER_ID, methodName);
		}
		if (profileImage.image == null || profileImage.image.length == 0) {
			return createInputParameterErrorDTO(Error.IMAGE_IS_NOT_SETTED, methodName);
		}
		if (profileImage.imageName == null || profileImage.imageName.equals("")) {
			return createInputParameterErrorDTO(Error.IMAGE_EMPTY_NAME, methodName);
		}
		if (profileImage.imageType == null) {
			return createInputParameterErrorDTO(Error.IMAGE_TYPE_IS_NOT_SETTED, methodName);
		}
		dto.message = Success.INPUT_PARAMETERS_IS_CORRECT.getMessage();
		dto.success = true;
		return dto;
	}

	private DTO createInputParameterErrorDTO(Error error, String methodName) {
		DTO errorDTO = new ObjectDTO<>();
		errorDTO.message = error.getMessage();
		_log.writeWarningMessage(error.getMessage(), "Class: ImageImpl, Method: " + methodName + "(input parameters)");
		return errorDTO;
	}

}
