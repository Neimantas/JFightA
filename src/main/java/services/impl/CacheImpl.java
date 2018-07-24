package services.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import models.business.Player;
import models.constant.Settings;
import models.dal.ItemDAL;
import models.dto.ListDTO;
import services.ICRUD;
import services.ICache;
import services.ILog;

public class CacheImpl implements ICache {
	private static ICache _cache = new CacheImpl();

	private static final int ONE_SECOND = 1000; // one second == 1000 ms
	private static final int ONE_MINUTE = ONE_SECOND * 60;
	private long _nextExpiredDataCleanUpTime;
	private Map<Integer, Player> _players; // Map<UserId, Player>
	private Map<Integer, ItemDAL> _items; // Map<ItemId, ItemDAL>
	private Map<Integer, Long> _itemExpireTime; // Map<ItemId, TimeInMs>
	private Set<Player> _readyPlayers;

	private CacheImpl() {
		_players = new ConcurrentHashMap<>();
		_items = new ConcurrentHashMap<>();
		_itemExpireTime = new ConcurrentHashMap<>();
		_readyPlayers = Collections.synchronizedSet(new HashSet<>());
		_nextExpiredDataCleanUpTime = System.currentTimeMillis()
				+ Settings.CACHE_EXPIRED_DATA_CLEANUP_PERIOD * ONE_MINUTE;
	}

	@Override
	public void addPlayer(int userId, Player player) {
		_players.put(userId, player);
	}

	@Override
	public void removePlayer(int userId) {
		_players.remove(userId);
	}

	@Override
	public Player getPlayer(int userId) {
		return _players.get(userId);
	}

	@Override
	public Player getPlayer(String userName) {
		for (Map.Entry<Integer, Player> player : _players.entrySet()) {
			if (userName.equals(player.getValue().user.name)) {
				return player.getValue();
			}
		}
		return null;
	}

	@Override
	public Set<Player> getReadyPlayersList() {
		return _readyPlayers;
	}

	@Override
	public boolean containsPlayer(String userName) {
		for (Map.Entry<Integer, Player> player : _players.entrySet()) {
			if (userName.equals(player.getValue().user.name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void addItem(ItemDAL itemDAL) {
		_items.put(itemDAL.itemId, itemDAL);
		_itemExpireTime.put(itemDAL.itemId,
				System.currentTimeMillis() + (ONE_SECOND * Settings.CACHE_ITEM_EXPIRE_TIME));
	}

	@Override
	public void removeItem(int itemId) {
		_items.remove(itemId);
		_itemExpireTime.remove(itemId);
	}

	@Override
	public ItemDAL getItem(int itemId) {
		if (_itemExpireTime.containsKey(itemId)) {
			_itemExpireTime.put(itemId, System.currentTimeMillis() + (ONE_SECOND * Settings.CACHE_ITEM_EXPIRE_TIME));
		}
		if (System.currentTimeMillis() > _nextExpiredDataCleanUpTime) {
			cleanUpExpiredData();
		}
		return _items.get(itemId);
	}

	public static ICache getInstance() {
		return _cache;
	}

	private void cleanUpExpiredData() {
		for (Map.Entry<Integer, Long> itemExpireTime : _itemExpireTime.entrySet()) {
			if (System.currentTimeMillis() > itemExpireTime.getValue()) {
				_itemExpireTime.remove(itemExpireTime.getKey());
				_items.remove(itemExpireTime.getKey());
			}
		}
		_nextExpiredDataCleanUpTime = System.currentTimeMillis()
				+ Settings.CACHE_EXPIRED_DATA_CLEANUP_PERIOD * ONE_MINUTE;
	}

}
