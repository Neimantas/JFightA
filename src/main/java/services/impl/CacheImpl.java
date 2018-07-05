package services.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import models.business.Player;
import services.ICache;

@Singleton
public class CacheImpl implements ICache {

	private Map<String, Player> players;
	private Set<Player> readyPlayers;

	public static ICache cache;

	public CacheImpl() {
		players = new ConcurrentHashMap<>();
		readyPlayers = Collections.synchronizedSet(new HashSet());
		cache = this;
	}

	@Override
	public void put(String userName, Player player) {
		players.put(userName, player);

	}

	@Override
	public Player get(String userName) {
		return players.get(userName);
	}

	@Override
	public void remove(String userName) {
		players.remove(userName);
	}

	@Override
	public Set<Player> getReadyUsersList() {
		return readyPlayers;
	}

	@Override
	public boolean containsUser(String userName) {
		return players.containsKey(userName);
	}

}
