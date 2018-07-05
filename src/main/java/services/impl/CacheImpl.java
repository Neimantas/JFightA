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

	public CacheImpl() {
		players = new ConcurrentHashMap<>();
		readyPlayers = Collections.synchronizedSet(new HashSet());
	}

	@Override
	public void put(String userName, Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Player get(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String userName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Player> getReadyUsersList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean containsUser(String userName) {
		// TODO Auto-generated method stub
		return false;
	}
	
	


}
