package services;

import java.util.Set;

import models.business.Player;
import models.dal.ItemDAL;

public interface ICache {

	void addPlayer(String userName, Player player);
	Player getPlayer(String userName);
	void removePlayer(String userName);
	Set<Player> getReadyPlayersList();
	boolean containsPlayer(String userName);
	ItemDAL getItem(int itemId);

}
