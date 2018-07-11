package services;

import models.dal.ImageDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IImage {
	
	public DTO uploadImage(ImageDAL imageDAL);

	public ObjectDTO<ImageDAL> getImage(int userId);

	public DTO deleteImage(int userId);

	public DTO startImageTransferSession();

	public DTO endImageTransferSession();

}
