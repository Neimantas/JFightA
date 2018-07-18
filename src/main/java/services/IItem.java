package services;

import models.dal.ImageDAL;
import models.dto.ObjectDTO;

public interface IItem {

	ObjectDTO<ImageDAL> getUserAImage(int userAId);
	ObjectDTO<ImageDAL> getUserBImage(int userBId);

}
