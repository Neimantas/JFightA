package services.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import models.business.Player;
import models.constant.Settings;
import models.constant.TimeMs;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import services.ICache;

public class CacheImpl implements ICache {
	private static ICache _cache = new CacheImpl();

	private long _nextExpiredDataCleanUpTime;
	private Map<Integer, Player> _players; // Map<UserId, Player>
	private Map<Integer, ImageDAL> _images; // Map<ImageId, ImageDAL>
	private Map<Integer, ItemDAL> _items; // Map<ItemId, ItemDAL>
	private Map<Integer, Long> _playerExpireTime; // Map<UserId, TimeInMs>
	private Map<Integer, Long> _imageExpireTime; // Map<ImageId, TimeInMs>
	private Map<Integer, Long> _itemExpireTime; // Map<ItemId, TimeInMs>

	private CacheImpl() {
		_players = new ConcurrentHashMap<>();
		_images = new ConcurrentHashMap<>();
		_items = new ConcurrentHashMap<>();
		_playerExpireTime = new ConcurrentHashMap<>();
		_imageExpireTime = new ConcurrentHashMap<>();
		_itemExpireTime = new ConcurrentHashMap<>();
		_nextExpiredDataCleanUpTime = System.currentTimeMillis()
				+ Settings.CACHE_EXPIRED_DATA_CLEANUP_PERIOD * TimeMs.MINUTE.getMilliseconds();
	}

	@Override
	public void addPlayer(Player player) {
		_players.put(player.user.userId, player);
		_playerExpireTime.put(player.user.userId,
				System.currentTimeMillis() + TimeMs.HOUR.getMilliseconds() * Settings.CACHE_PLAYER_EXPIRE_TIME);
	}

	@Override
	public void removePlayer(int userId) {
		_players.remove(userId);
		_playerExpireTime.remove(userId);
	}

	@Override
	public Player getPlayer(int userId) {
		if (_playerExpireTime.containsKey(userId)) { // if player exists in a cache
			_playerExpireTime.put(userId,
					System.currentTimeMillis() + TimeMs.HOUR.getMilliseconds() * Settings.CACHE_PLAYER_EXPIRE_TIME);
		}
		return _players.get(userId); // return null if player doesn't exists
	}

	@Override
	public Map<Integer, Player> getPlayers() {
		return new HashMap<>(_players);
	}

	@Override
	public void addItem(ItemDAL itemDAL) {
		_items.put(itemDAL.itemId, itemDAL);
		_itemExpireTime.put(itemDAL.itemId,
				System.currentTimeMillis() + TimeMs.SECOND.getMilliseconds() * Settings.CACHE_IMAGE_AND_ITEM_EXPIRE_TIME);
	}

	@Override
	public void removeItem(int itemId) {
		_items.remove(itemId);
		_itemExpireTime.remove(itemId);
	}

	@Override
	public ItemDAL getItem(int itemId) {
		if (_itemExpireTime.containsKey(itemId)) { // if item exists in a cache
			_itemExpireTime.put(itemId, System.currentTimeMillis()
					+ TimeMs.SECOND.getMilliseconds() * Settings.CACHE_IMAGE_AND_ITEM_EXPIRE_TIME);
		}
		return _items.get(itemId); // return null if item doesn't exists
	}

	@Override
	public void addImage(ImageDAL imageDAL) {
		_images.put(imageDAL.imageId, imageDAL);
		_imageExpireTime.put(imageDAL.imageId,
				System.currentTimeMillis() + TimeMs.SECOND.getMilliseconds() * Settings.CACHE_IMAGE_AND_ITEM_EXPIRE_TIME);
	}

	@Override
	public void removeImage(int imageId) {
		_images.remove(imageId);
		_imageExpireTime.remove(imageId);
	}

	@Override
	public ImageDAL getImage(int imageId) {
		if (_imageExpireTime.containsKey(imageId)) { // if image exists in a cache
			_imageExpireTime.put(imageId, System.currentTimeMillis()
					+ TimeMs.SECOND.getMilliseconds() * Settings.CACHE_IMAGE_AND_ITEM_EXPIRE_TIME);
		}
		if (System.currentTimeMillis() > _nextExpiredDataCleanUpTime) {
			cleanUpExpiredData();
		}
		return _images.get(imageId); // return null if image doesn't exists
	}

	public static ICache getInstance() {
		return _cache;
	}

	private void cleanUpExpiredData() {
		// Clean up players if expired
		for (Iterator<Map.Entry<Integer, Long>> playerIterator = _playerExpireTime.entrySet().iterator();
				playerIterator.hasNext();) {
			Map.Entry<Integer, Long> playerEntry = playerIterator.next();
			if (System.currentTimeMillis() > playerEntry.getValue()) {
				_players.remove(playerEntry.getKey());
				playerIterator.remove();
			}
		}
		// Clean up images if expired
		for (Iterator<Map.Entry<Integer, Long>> imageIterator = _imageExpireTime.entrySet().iterator();
				imageIterator.hasNext();) {
			Map.Entry<Integer, Long> imageEntry = imageIterator.next();
			if (System.currentTimeMillis() > imageEntry.getValue()) {
				_images.remove(imageEntry.getKey());
				imageIterator.remove();
			}
		}
		// Clean up items if expired
		for (Iterator<Map.Entry<Integer, Long>> itemIterator = _itemExpireTime.entrySet().iterator(); 
				itemIterator.hasNext();) {
			Map.Entry<Integer, Long> itemEntry = itemIterator.next();
			if (System.currentTimeMillis() > itemEntry.getValue()) {
				_items.remove(itemEntry.getKey());
				itemIterator.remove();
			}
		}

		_nextExpiredDataCleanUpTime = System.currentTimeMillis()
				+ Settings.CACHE_EXPIRED_DATA_CLEANUP_PERIOD * TimeMs.MINUTE.getMilliseconds();
	}

}
