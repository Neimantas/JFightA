package services.impl;

import models.business.CharacterInfo;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.UserDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import models.dto.UserDTO;
import services.IHigherLoginService;

public class HigherLoginService implements IHigherLoginService {

	CRUDImpl crud;

	public HigherLoginService() {
		crud = new CRUDImpl(new DatabaseImpl());
	}

	@Override
	public UserDTO login(UserLoginData userIn) {
		UserDAL userInDal = new UserDAL();
		userInDal.name = userIn.name;
		userInDal.password = userIn.password;
		ListDTO userDTO = crud.read(userInDal);
		if (userDTO.success) {
			UserDAL userDal = (UserDAL) userDTO.transferDataList.get(0);
			CharacterInfo takeCharacterData = new CharacterInfo();
			takeCharacterData.userId = userDal.userId;
			return new UserDTO(true, "success", userDal);
		}
		return new UserDTO(false, crud.read(userInDal).message, null);
	}

	@Override
	public UserDTO registration(UserRegIn userRegIn) {
		UserDAL userInDal = new UserDAL();
		userInDal.name = userRegIn.name;
		userInDal.password = userRegIn.password;
		userInDal.eMail = userRegIn.mail;
		ObjectDTO newUserDto = crud.create(userInDal);
		if (newUserDto.success) {
			UserDAL newUserDal = (UserDAL) newUserDto.transferData;
			CharacterInfo newCharacter = new CharacterInfo();
			newCharacter.userId = newUserDal.userId;
			newCharacter.healthPoints = 100;
			newCharacter.strenght = 5;
			newCharacter.experience = 0;
			newCharacter.level = 1;

		}
		return null;
	}

}
