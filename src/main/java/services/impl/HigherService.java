
package services.impl;

import models.business.DefaultCharacter;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.constant.Error;
import models.constant.Success;
import models.dal.CharacterDAL;
import models.dal.FightDataDAL;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dal.LogDAL;
import models.dal.PlayerDAL;
import models.dal.ResultDAL;
import models.dal.UserDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IHigherService;
import services.ILog;

public class HigherService implements IHigherService {

	private ICRUD _crud;
	private ILog _log;

	public HigherService(CRUDImpl inputCrud) {
		_crud = inputCrud;
		_log = LogImpl.getInstance();
	}

	@Override
	public ObjectDTO login(UserLoginData userIn) {
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
					return new ObjectDTO(true, "success", player);
				}
				return new ObjectDTO(false, charDTO.message, null);
			}
		}
		return new ObjectDTO(false, userDTO.message, null);
	}

	@Override
	public ObjectDTO<UserLoginData> registration(UserRegIn userRegIn) {
		// Fills new user info
		UserDAL userInDal = new UserDAL();
		userInDal.name = userRegIn.name;
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
				return new ObjectDTO<UserLoginData>(true, "success", user);
			}
			return new ObjectDTO<UserLoginData>(false, characterCreat.message, null);
		}
		return new ObjectDTO<UserLoginData>(false, newUserDto.message, null);
	}

	@Override
	public String hashPassword(String password) {
		String passwordHased = "";
		HashService hashPassword = new HashService();
		try {
			passwordHased = hashPassword.getSaltedHash(password);
		} catch (Exception e) {
			_log.writeErrorMessage(e);
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
			_log.writeErrorMessage(e);
		}
		return false;
	}

	@Override
	public Integer getNewFightId() {
		ResultDAL resultdal = new ResultDAL();
		ICRUD crud = new CRUDImpl();
		// Get unused fightID from DB.
		return crud.create(resultdal).transferData;
	}

	@Override
	public ListDTO<FightDataDAL> getFightDataDAL(int fightId) {
		FightDataDAL dalF = new FightDataDAL();
		dalF.fightId = fightId;
		ListDTO<FightDataDAL> dtoF = _crud.<FightDataDAL>read(dalF);
		return dtoF;
	}

	@Override
	public ListDTO<LogDAL> logInfoDAL(int userIdA, int userIdB) {
		LogDAL dalL = new LogDAL();
		dalL.user1Id = userIdA;
		dalL.user2Id = userIdB;
		ListDTO<LogDAL> dtoLog = _crud.<LogDAL>read(dalL);
		return dtoLog;
	}

	@Override
	public void writeFightResult(int fightId, int winPlayerId, int losePlayerId, boolean draw) {
		if (!draw) {
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

	@Override
	public ObjectDTO<UserDAL> getUser(int userId) {
		UserDAL userDAL = new UserDAL();
		userDAL.userId = userId;
		ListDTO<UserDAL> listDTO = _crud.read(userDAL);
		ObjectDTO<UserDAL> userDTO = new ObjectDTO<>();
		if (listDTO.success && !listDTO.transferDataList.isEmpty()) {
			userDTO.transferData = listDTO.transferDataList.get(0);
			userDTO.message = listDTO.message;
			userDTO.success = true;
			return userDTO;
		} else {
			_log.writeWarningMessage(Error.USER_WASNT_DOWNLOADED_FROM_DB.getMessage(), "User No " + userId,
					"Class: HigherService, method: ObjectDTO<UserDAL> getUser(int userId).",
					"crud read message: " + listDTO.message);
			userDTO.message = listDTO.message;
			return userDTO;
		}
	}

	@Override
	public ObjectDTO<Integer> logFightDataDAL(int fightId, int userIdA, int userIdB, String json) {
		LogDAL dalL = new LogDAL();
		dalL.fightId = fightId;
		dalL.user1Id = userIdA;
		dalL.user2Id = userIdB;
		dalL.log = json;
		ObjectDTO<Integer> dtoCreate = _crud.<LogDAL>create(dalL);
		return dtoCreate;
	}

	@Override
	public ObjectDTO<UserDAL> updateUserImageId(int userId, int imageId) {
		ObjectDTO<UserDAL> userDTO = getUser(userId);
		if (userDTO.success) {
			UserDAL userDAL = userDTO.transferData;
			userDAL.imageId = imageId;
			DTO dto = _crud.update(userDAL);
			if (dto.success) {
				userDTO.transferData = userDAL;
			}
			userDTO.message = dto.message;
			userDTO.success = dto.success;
			return userDTO;
		}
		return userDTO;
	}

	@Override
	public ObjectDTO<ItemDAL> getItem(int itemId) {
		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemId = itemId;
		ListDTO<ItemDAL> listDTO = _crud.read(itemDAL);
		ObjectDTO<ItemDAL> itemDTO = new ObjectDTO<>();
		if (listDTO.success && !listDTO.transferDataList.isEmpty()) {
			itemDTO.transferData = listDTO.transferDataList.get(0);
			itemDTO.message = listDTO.message;
			itemDTO.success = true;
			return itemDTO;
		} else {
			_log.writeWarningMessage(Error.ITEM_WASNT_DOWNLOADED_FROM_DB.getMessage(), "Item No " + itemId,
					"Class: HigherService, method: ObjectDTO<ItemDAL> getItem(int itemId).",
					"crud read message: " + listDTO.message);
			itemDTO.message = listDTO.message;
			return itemDTO;
		}
	}

	@Override
	public ObjectDTO<Integer> createNewItem(ItemDAL itemDAL) {
		return _crud.create(itemDAL);
	}

	@Override
	public DTO editItem(ItemDAL itemDAL) {
		return _crud.update(itemDAL);
	}

	@Override
	public DTO deleteItem(ItemDAL itemDAL) {
		return _crud.delete(itemDAL);
	}

	@Override
	public ObjectDTO<ImageDAL> getImage(int imageId) {
		ObjectDTO<ImageDAL> imageDTO = new ObjectDTO<>();
		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageId;
		ListDTO<ImageDAL> imageListDTO = _crud.read(imageDAL);
		if (imageListDTO.success == true && !imageListDTO.transferDataList.isEmpty()) {
			imageDTO.transferData = imageListDTO.transferDataList.get(0);
		}
		imageDTO.message = imageListDTO.message;
		imageDTO.success = imageListDTO.success;
		return imageDTO;
	}

	@Override
	public ObjectDTO<Integer> getImageId(int userId) {
		ObjectDTO<Integer> imageIdDTO = new ObjectDTO<>();
		UserDAL userDAL = new UserDAL();
		userDAL.userId = userId;
		ListDTO<UserDAL> listDTO = _crud.read(userDAL);
		if (listDTO.success) {
			if (!listDTO.transferDataList.isEmpty() && listDTO.transferDataList.get(0).imageId != null) {
				imageIdDTO.transferData = listDTO.transferDataList.get(0).imageId;
				imageIdDTO.message = Success.USER_HAS_AN_IMAGE.getMessage();
				imageIdDTO.success = true;
				return imageIdDTO;
			}
			imageIdDTO.message = Error.USER_HAS_NO_IMAGE.getMessage();
			return imageIdDTO;
		}
		imageIdDTO.message = listDTO.message;
		return imageIdDTO;
	}

	@Override
	public ObjectDTO<Integer> createNewImage(ImageDAL imageDAL) {
		return _crud.create(imageDAL);
	}

	@Override
	public DTO updateImage(ImageDAL imageDAL) {
		return _crud.update(imageDAL);
	}

	@Override
	public DTO deleteImage(ImageDAL imageDAL) {
		return _crud.delete(imageDAL);
	}

}
