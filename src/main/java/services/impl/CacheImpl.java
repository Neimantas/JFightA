package services.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import models.business.Player;
import services.ICache;

public class CacheImpl implements ICache {
	private static ICache _cache = new CacheImpl();

	private Map<String, Player> _players;
	private Set<Player> _readyPlayers;

	private CacheImpl() {
		_players = new ConcurrentHashMap<>();
		_readyPlayers = Collections.synchronizedSet(new HashSet());
	}

	@Override
	public void put(String userName, Player player) {
		_players.put(userName, player);

	}

	@Override
	public Player get(String userName) {
		return _players.get(userName);
	}

	@Override
	public void remove(String userName) {
		_players.remove(userName);
	}

	@Override
	public Set<Player> getReadyUsersList() {
		return _readyPlayers;
	}

	@Override
	public boolean containsUser(String userName) {
		return _players.containsKey(userName);
	}

	public static ICache getInstance() {
		return _cache;
	}

}
