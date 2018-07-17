package services;

import models.dal.ImageDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IImage {

	DTO uploadImage(ImageDAL imageDAL);
	ObjectDTO<ImageDAL> getImage(int userId);
	DTO deleteImage(int userId);

}
