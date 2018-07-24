package services;

import java.util.Set;

import models.business.Player;
import models.dal.ItemDAL;

public interface ICache {

	void addPlayer(int userId, Player player);
	void removePlayer(int userId);
	Player getPlayer(int userId);
	Player getPlayer(String userName);
	Set<Player> getReadyPlayersList();
	boolean containsPlayer(String userName);
	void addItem(ItemDAL itemDAL);
	void removeItem(int itemId);
	ItemDAL getItem(int itemId);

}
