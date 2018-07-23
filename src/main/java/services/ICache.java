package services;

import java.util.Map;
import java.util.Set;

import models.business.Player;
import models.dal.ItemDAL;

public interface ICache {

	void addPlayer(String userName, Player player);
	Player getPlayer(String userName);
	Player getPlayer(int userId);
	Map<String, Player> getLoggedInPlayers();
	void removePlayer(String userName);
	Set<Player> getReadyPlayersList();
	boolean containsPlayer(String userName);
	ItemDAL getItem(int itemId);

}
