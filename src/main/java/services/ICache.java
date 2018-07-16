package services;

import java.util.Set;

import models.business.Player;

public interface ICache {

	void put(String userName, Player player);
	Player get(String userName);
	void remove(String userName);
	Set<Player> getReadyUsersList();
	boolean containsUser(String userName);

}
