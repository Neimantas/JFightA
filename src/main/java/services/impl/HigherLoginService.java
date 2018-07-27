
package services.impl;

import javax.servlet.http.Cookie;

import models.business.CharacterInfo;
import models.business.Player;
import models.business.User;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.CharacterDAL;
import models.dal.PlayerDAL;
import models.dal.UserDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import models.dto.PlayerDalDTO;
import models.dto.UserDTO;
import services.ICRUD;
import services.ICache;
import services.IHigherLoginService;

public class HigherLoginService implements IHigherLoginService {

	ICRUD crud;
	ICache cashe;

	public HigherLoginService(CRUDImpl inputCrud) {
		crud = inputCrud;
		cashe = CacheImpl.getInstance();
	}

	@Override
	public PlayerDalDTO login(UserLoginData userIn) {
		UserDAL userInDal = new UserDAL();
		userInDal.name = userIn.name;
		userInDal.password = userIn.password;
		ListDTO<UserDAL> userDTO = crud.read(userInDal);
		// Takes UserDal
		if (userDTO.success && !userDTO.transferDataList.isEmpty()) {
			UserDAL userDal = userDTO.transferDataList.get(0);
			// Makes charDal and fills just userID field, it need to get chat form CRUD
			CharacterDAL takeCharacterData = new CharacterDAL();
			takeCharacterData.userId = userDal.userId;
			ListDTO<CharacterDAL> charDTO = crud.read(takeCharacterData);
			// Takes charDal
			if (charDTO.success) {
				CharacterDAL charDal = charDTO.transferDataList.get(0);
				// Creating PlayerDAL and add char & user
				PlayerDAL player = new PlayerDAL();
				player.characterDal = charDal;
				player.userDal = userDal;
				return new PlayerDalDTO(true, "success", player);
			}
			return new PlayerDalDTO(false, charDTO.message, null);
		}
		return new PlayerDalDTO(false, userDTO.message, null);
	}

	@Override
	public PlayerDalDTO registration(UserRegIn userRegIn) {
		//Fills new user info
		UserDAL userInDal = new UserDAL();
		userInDal.name = userRegIn.name;
		userInDal.password = userRegIn.password;
		userInDal.eMail = userRegIn.mail;
		//Creates new user 
		crud.create(userInDal);
		//Read new created user
		ListDTO<UserDAL> newUserDto = crud.read(userInDal);		
		if (newUserDto.success) {
			//Fills new char info
			userInDal.userId = newUserDto.transferDataList.get(0).userId;
			CharacterDAL newCharacter = new CharacterDAL();
			newCharacter.userId = userInDal.userId;
			newCharacter.healthPoints = 100;
			newCharacter.strenght = 5;
			newCharacter.experience = 0;
			newCharacter.level = 1;
			//Creates mew char
			ObjectDTO<Integer> characterCreat = crud.create(newCharacter);
			if (characterCreat.success) {
				UserLoginData user = new UserLoginData();
				user.name = userInDal.name;
				user.password = userInDal.password;
				return new PlayerDalDTO(true, "success",null);
			}
			return new PlayerDalDTO(false, characterCreat.message,null);
		}
		return new PlayerDalDTO(false, newUserDto.message,null);
	}
}
