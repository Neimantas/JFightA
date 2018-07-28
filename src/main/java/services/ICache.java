package services;

import java.util.Map;

import models.business.Player;
import models.dal.ImageDAL;
import models.dal.ItemDAL;

public interface ICache {

	void addPlayer(Player player);
	void removePlayer(int userId);
	Player getPlayer(int userId);
	Map<Integer, Player> getPlayers();
	void addItem(ItemDAL itemDAL);
	void removeItem(int itemId);
	ItemDAL getItem(int itemId);
	void addImage(ImageDAL imageDAL);
	void removeImage(int imageId);
	ImageDAL getImage(int imagemId);

}
