import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import models.business.CharacterInfo;
import models.business.Player;
import models.business.User;
import models.constant.ItemType;
import models.constant.UserStatus;
import models.dal.CharacterDAL;
import models.dal.ItemDAL;
import models.dal.UserDAL;
import models.dto.ListDTO;
import services.ICRUD;
import services.ICache;
import services.IItem;
import services.impl.CRUDImpl;
import services.impl.CacheImpl;
import services.impl.ItemImpl;

public class ItemTest {

	public static void main(String[] args) {

//		testDeleteImage();
//		try {
//			// testAddImage();
//			testEditImage();
//		} catch (IOException e) {
//		}

	}

	private static void testGetUserItems() {

		ICRUD crud = new CRUDImpl();
		ICache cache = CacheImpl.getInstance();
		IItem item = new ItemImpl(new CRUDImpl());

		UserDAL userDAL = new UserDAL();
		userDAL.name = "labas";
		userDAL.password = "krabas";
		ListDTO<UserDAL> ldto = crud.read(userDAL);
		User user = new User();
		user.userId = ldto.transferDataList.get(0).userId;
		user.name = ldto.transferDataList.get(0).name;

		CharacterDAL characterDAL = new CharacterDAL();
		characterDAL.userId = user.userId;
		ListDTO<CharacterDAL> cldto = crud.read(characterDAL);
		CharacterInfo characterInfo = new CharacterInfo();
		characterInfo.userId = cldto.transferDataList.get(0).userId;
		characterInfo.attackItemId = cldto.transferDataList.get(0).attackItemId;
		characterInfo.defenceItemId = cldto.transferDataList.get(0).defenceItemId;

		Player player = new Player();
		player.user = user;
		player.characterInfo = characterInfo;
		player.userStatus = UserStatus.NOT_READY;

		cache.addPlayer(player);

		Map<ItemType, ItemDAL> items = item.getUserItems(player.user.userId);

		System.out.println(items.get(ItemType.ATTACK).itemId);
		System.out.println(items.get(ItemType.DEFENCE).itemId);

	}

	private static void testAddImage() throws IOException {

		IItem item = new ItemImpl(new CRUDImpl());

		File file = new File("src\\main\\webapp\\resources\\images\\characters\\Alex.png");
		byte[] image = Files.readAllBytes(file.toPath());
		String imageName = "Alex.png";
		item.addImage(11, imageName, image);

	}
	
	private static void testEditImage() throws IOException {
		
		IItem item = new ItemImpl(new CRUDImpl());

		File file = new File("src\\main\\webapp\\resources\\images\\characters\\chun-li.jpg");
		byte[] image = Files.readAllBytes(file.toPath());
		String imageName = "chun-li.jpg";
		item.editImage(11, imageName, image);
	}
	
	private static void testDeleteImage() {
		
		IItem item = new ItemImpl(new CRUDImpl());
		item.deleteImage(10);
		
	}
}
