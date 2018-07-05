package services;

import java.util.Set;

import models.business.Player;

public interface ICache {

	public void put(String userName, Player player);

	public Player get(String userName);

	public void remove(String userName);

	public Set<Player> getReadyUsersList();

	public boolean containsUser(String userName);

}
