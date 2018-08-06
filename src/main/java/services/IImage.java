package services;

import models.business.ProfileImage;
import models.dto.DTO;
import models.dto.ObjectDTO;

public interface IImage {

	ObjectDTO<ProfileImage> getUserAImage(int userAId);
	ObjectDTO<ProfileImage> getUserBImage(int userBId);
	ObjectDTO<Integer> addImage(ProfileImage profileImage);
	DTO editImage(ProfileImage profileImage);
	DTO editDefaultImage(ProfileImage profileImage);
	DTO deleteImage(int userId);

}
