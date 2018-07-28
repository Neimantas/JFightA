package services;

import models.dal.ImageDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IImage {

	ObjectDTO<ImageDAL> getUserAImage(int userAId);
	ObjectDTO<ImageDAL> getUserBImage(int userBId);
	ObjectDTO<Integer> addImage(int userId, String imageName, byte[] image);
	DTO editImage(int userId, String imageName, byte[] image);
	DTO editDefaultImage(int imageId, String imageName, byte[] image);
	DTO deleteImage(int userId);

}
