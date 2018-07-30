package services;

import models.constant.ImageType;
import models.dal.ImageDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IImage {

	ObjectDTO<ImageDAL> getUserAImage(int userAId);
	ObjectDTO<ImageDAL> getUserBImage(int userBId);
	ObjectDTO<Integer> addImage(int userId, String imageName, ImageType imageType, byte[] image);
	DTO editImage(int userId, String imageName, ImageType imageType, byte[] image);
	DTO editDefaultImage(int imageId, String imageName, ImageType imageType, byte[] image);
	DTO deleteImage(int userId);

}
