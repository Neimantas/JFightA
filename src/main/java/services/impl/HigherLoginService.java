
package services.impl;

import models.business.DefaultCharacter;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.CharacterDAL;
import models.dal.PlayerDAL;
import models.dal.UserDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import models.dto.PlayerDalDTO;
import models.dto.UserLoginDataDTO;
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
	public UserLoginDataDTO registration(UserRegIn userRegIn) {
		//Fills new user info
		UserDAL userInDal = new UserDAL();
		userInDal.name = userRegIn.name;
		userInDal.password = userRegIn.password;
		userInDal.eMail = userRegIn.mail;
		userInDal.accessLevel=1;
		userInDal.imageId=20;
		//Creates new user 
		crud.create(userInDal);
		//Read new created user
		ListDTO<UserDAL> newUserDto = crud.read(userInDal);		
		if (newUserDto.success) {
			//Fills new char info
			userInDal.userId = newUserDto.transferDataList.get(0).userId;
			CharacterDAL newCharacter = new CharacterDAL();
			DefaultCharacter defaul=new DefaultCharacter();
			newCharacter.userId = userInDal.userId;
			newCharacter.healthPoints = defaul.healthPoints;
			newCharacter.strenght = defaul.strenght;
			newCharacter.experience = defaul.experience;
			newCharacter.level = defaul.level;
			newCharacter.attackItemId=defaul.attackItemId;
			newCharacter.defenceItemId=defaul.defenceItemId;
			//Creates mew char
			ObjectDTO<Integer> characterCreat = crud.create(newCharacter);
			if (characterCreat.success) {
				System.out.println("pateko");
				UserLoginData user = new UserLoginData();
				user.name = userInDal.name;
				user.password = userInDal.password;
				return new UserLoginDataDTO(true, "success",user);
			}
			return new UserLoginDataDTO(false, characterCreat.message,null);
		}
		return new UserLoginDataDTO(false, newUserDto.message,null);
	}
}
