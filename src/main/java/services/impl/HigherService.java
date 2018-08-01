
package services.impl;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import models.business.DefaultCharacter;
import models.business.Item;
import models.business.ProfileImage;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.constant.Error;
import models.constant.Success;
import models.dal.CharacterDAL;
import models.dal.FightDataDAL;
import models.dal.LogDAL;
import models.dal.PlayerDAL;
import models.dal.ResultDAL;
import models.dal.UserDAL;
import models.dto.DTO;
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
		// Get unused fightID from DB.
		return crud.create(resultdal).transferData;
	}
	
	@Override
	public ListDTO<FightDataDAL> logFightDataDAL(int fightId) {

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
			_log.writeWarningMessage(Error.USER_WASNT_DOWNLOADED_FROM_DB.getMessage(), true, "User No " + userId,
					"Class: HigherService, method: ObjectDTO<UserDAL> getUser(int userId).",
					"crud read message: " + listDTO.message);
			userDTO.message = listDTO.message;
			return userDTO;
		}
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
			_log.writeWarningMessage(Error.ITEM_WASNT_DOWNLOADED_FROM_DB.getMessage(), true, "Item No " + itemId,
					"Class: HigherService, method: ObjectDTO<ItemDAL> getItem(int itemId).",
					"crud read message: " + listDTO.message);
			itemDTO.message = listDTO.message;
			return itemDTO;
		}
	}

	@Override
	public ObjectDTO<Integer> createNewItem(Item item) {
		return _crud.create(itemToItemDAL(item));
	}

	@Override
	public DTO editItem(Item item) {
		return _crud.update(itemToItemDAL(item));
	}

	@Override
	public DTO deleteItem(int itemId) {
		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemId = itemId;
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
	public ObjectDTO<Integer> createNewImage(ProfileImage profileImage) {
		return _crud.create(profileImageToImageDAL(profileImage));
	}

	@Override
	public DTO updateImage(ProfileImage profileImage) {
		return _crud.update(profileImageToImageDAL(profileImage));
	}

	@Override
	public DTO deleteImage(int imageId) {
		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageId = imageId;
		return _crud.delete(imageDAL);
	}

	private ItemDAL itemToItemDAL(Item item) {
		ItemDAL itemDAL = new ItemDAL();
		if (item != null) {
			itemDAL.itemId = item.itemId != 0 ? item.itemId : null;
			itemDAL.itemName = (item.itemName != null && !item.itemName.equals("")) ? item.itemName : null;
			itemDAL.itemImage = item.itemImage;
			itemDAL.imageFormat = item.imageFormat.getImageExtension();
			itemDAL.itemType = item.itemType;
			itemDAL.description = (item.description != null && !item.description.equals("")) ? item.description : null;
			itemDAL.minCharacterLevel = item.minCharacterLevel != 0 ? item.minCharacterLevel : null;
			itemDAL.attackPoints = item.attackPoints != 0 ? item.attackPoints : null;
			itemDAL.defencePoints = item.defencePoints != 0 ? item.defencePoints : null;
		}
		return itemDAL;
	}

	private ImageDAL profileImageToImageDAL(ProfileImage profileImage) {
		ImageDAL imageDAL = new ImageDAL();
		if (profileImage != null) {
			imageDAL.imageId = profileImage.imageId != 0 ? profileImage.imageId : null;
			imageDAL.userId = profileImage.userId != 0 ? profileImage.userId : null;
			imageDAL.image = profileImage.image;
			imageDAL.imageName = (profileImage.imageName != null ? profileImage.imageName : "")
					+ (profileImage.imageType != null ? profileImage.imageType.getImageExtension() : "");
			if (imageDAL.imageName.equals("")) {
				imageDAL.imageName = null;
			}
		}
		return imageDAL;
	}

}
