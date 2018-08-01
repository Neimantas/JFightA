package services;

import models.business.ProfileImage;
import models.constant.ImageType;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IImage {

	ObjectDTO<ProfileImage> getUserAImage(int userAId);
	ObjectDTO<ProfileImage> getUserBImage(int userBId);
	ObjectDTO<Integer> addImage(int userId, String imageName, ImageType imageType, byte[] image);
	DTO editImage(int userId, String imageName, ImageType imageType, byte[] image);
	DTO editDefaultImage(int imageId, String imageName, ImageType imageType, byte[] image);
	DTO deleteImage(int userId);

}
