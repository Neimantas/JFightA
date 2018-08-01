
package services.impl;

import models.business.DefaultCharacter;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.CharacterDAL;
import models.dal.PlayerDAL;
import models.dal.ResultDAL;
import models.dal.UserDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import models.dto.PlayerDalDTO;
import models.dto.UserLoginDataDTO;
import services.ICRUD;
import services.ICache;
import services.IHigherService;
import services.ILog;

public class HigherService implements IHigherService {

	private ICRUD _crud;
	private ICache _cache;
	private ILog _log;

	public HigherService(CRUDImpl inputCrud) {
		_crud = inputCrud;
		_cache = CacheImpl.getInstance();
		_log = LogImpl.getInstance();
	}

	@Override
	public PlayerDalDTO login(UserLoginData userIn) {
		UserDAL userInDal = new UserDAL();
		userInDal.name = userIn.name;
		// userInDal.password = userIn.password;

		ListDTO<UserDAL> userDTO = _crud.read(userInDal);

		// Takes UserDal
		if (userDTO.success && !userDTO.transferDataList.isEmpty()) {
			UserDAL userDal = userDTO.transferDataList.get(0);

			if (passwordCheck(userIn.password, userDal.password)) {
				// Makes charDal and fills just userID field, it need to get chat form CRUD
				CharacterDAL takeCharacterData = new CharacterDAL();
				takeCharacterData.userId = userDal.userId;
				ListDTO<CharacterDAL> charDTO = _crud.read(takeCharacterData);
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
			;
		}
		return new PlayerDalDTO(false, userDTO.message, null);
	}

	@Override
	public UserLoginDataDTO registration(UserRegIn userRegIn) {
		// Fills new user info
		UserDAL userInDal = new UserDAL();
		userInDal.name=userRegIn.name;
		userInDal.password = hashPassword(userRegIn.password);
		userInDal.eMail = userRegIn.mail;
		userInDal.accessLevel = 1;
		// Creates new user
		_crud.create(userInDal);
		// Read new created user
		ListDTO<UserDAL> newUserDto = _crud.read(userInDal);
		if (newUserDto.success) {
			// Fills new char info
			userInDal.userId = newUserDto.transferDataList.get(0).userId;
			CharacterDAL newCharacter = new CharacterDAL();
			DefaultCharacter defaul = new DefaultCharacter();
			newCharacter.userId = userInDal.userId;
			newCharacter.healthPoints = defaul.healthPoints;
			newCharacter.strenght = defaul.strenght;
			newCharacter.experience = defaul.experience;
			newCharacter.level = defaul.level;
			newCharacter.attackItemId = defaul.attackItemId;
			newCharacter.defenceItemId = defaul.defenceItemId;
			// Creates mew char
			ObjectDTO<Integer> characterCreat = _crud.create(newCharacter);
			if (characterCreat.success) {
				System.out.println("pateko");
				UserLoginData user = new UserLoginData();
				user.name = userInDal.name;
				user.password = userInDal.password;
				return new UserLoginDataDTO(true, "success", user);
			}
			return new UserLoginDataDTO(false, characterCreat.message, null);
		}
		return new UserLoginDataDTO(false, newUserDto.message, null);
	}

	@Override
	public String hashPassword(String password) {
		String passwordHased = "";
		HashService hashPassword = new HashService();
		try {
			passwordHased = hashPassword.getSaltedHash(password);
		} catch (Exception e) {
			_log.writeErrorMessage(e, true);
		}
		return passwordHased;

	}

	@Override
	public boolean passwordCheck(String password, String paswordInDB) {
		HashService hashPassword = new HashService();
		try {
			if (hashPassword.check(password, paswordInDB)) {
				return true;
			}
		} catch (Exception e) {
			_log.writeErrorMessage(e, true);
		}
		return false;
	}

	@Override
	public Integer getNewFightId() {
		ResultDAL resultdal = new ResultDAL();
		ICRUD crud = new CRUDImpl();
		//Get unused fightID from DB.
		return crud.create(resultdal).transferData;
	}

	@Override
	public void writeFightResult(int fightId, int winPlayerId, int losePlayerId, boolean draw) {
		if(!draw) {
			ResultDAL result = new ResultDAL();
			result.fightId = fightId;
			result.winUserId = winPlayerId;
			result.lossUserId = losePlayerId;
			System.out.println(_crud.update(result).message);
		} else {
			ResultDAL result = new ResultDAL();
			result.fightId = fightId;
			result.tieUser1Id = winPlayerId;
			result.tieUser2Id = losePlayerId;
			_crud.update(result);
		}
	}

}
