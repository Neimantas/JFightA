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
import services.impl.DatabaseImpl;
import services.impl.ItemImpl;

public class ItemTest {

	public static void main(String[] args) {
		
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
		
		cache.addPlayer(player.user.userId, player);
		
		Map<ItemType, ItemDAL> items = item.getUserItems(player.user.userId);
		
		System.out.println(items.get(ItemType.ATTACK).itemId);
		System.out.println(items.get(ItemType.DEFENCE).itemId);

	}

}
