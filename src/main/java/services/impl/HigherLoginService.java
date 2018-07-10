package services.impl;

import models.business.CharacterInfo;
import models.business.User;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.CharacterDAL;
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
		ListDTO<UserDAL> userDTO = crud.read(userInDal);

		if (userDTO.success) {
			UserDAL userDal = userDTO.transferDataList.get(0);
			CharacterInfo takeCharacterData = new CharacterInfo();
			takeCharacterData.userId = userDal.userId;
			return new UserDTO(true, "success", userDal);
		}
		return new UserDTO(false, userDTO.message, null);
	}

	@Override
	public UserDTO registration(UserRegIn userRegIn) {
		UserDAL userInDal = new UserDAL();
		userInDal.name = userRegIn.name;
		userInDal.password = userRegIn.password;
		userInDal.eMail = userRegIn.mail;
		ObjectDTO<UserDAL> newUserDto = crud.create(userInDal);
		if (newUserDto.success) {
			UserDAL newUserDal = newUserDto.transferData;
			CharacterDAL newCharacter = new CharacterDAL();
			newCharacter.userId = newUserDal.userId;
			newCharacter.healthPoints = 100;
			newCharacter.strenght = 5;
			newCharacter.experience = 0;
			newCharacter.level = 1;
			crud.create(newCharacter);
			UserLoginData user = new UserLoginData();
			user.name = userInDal.name;
			user.password = userInDal.password;
			UserDTO prilog=login(user);
			if(prilog._success) {			
				return new UserDTO(true, "success", prilog._userDal);
			}
			return new UserDTO(false, prilog._message, null);
		}
		return new UserDTO(false, newUserDto.message, null);
	}

	@Override
	public void logout(User user) {
		
	}

}
