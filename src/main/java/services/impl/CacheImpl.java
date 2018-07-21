package services.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import models.business.Player;
import models.dal.ItemDAL;
import models.dto.ListDTO;
import services.ICRUD;
import services.ICache;
import services.ILog;

public class CacheImpl implements ICache {
	private static ICache _cache = new CacheImpl();

	private ICRUD _crud = CRUDImpl.getInstance();
	private ILog _log = LogImpl.getInstance();
	private Map<String, Player> _players;
	private Map<Integer, ItemDAL> _items;
	private Map<Integer, Integer> _itemUseCounter; // Map<ItemId, numberOfPlayersAreCurrentlyUsing>
	private Set<Player> _readyPlayers;

	private CacheImpl() {
		_players = new ConcurrentHashMap<>();
		_items = new ConcurrentHashMap<>();
		_readyPlayers = Collections.synchronizedSet(new HashSet<>());
	}

	@Override
	public void addPlayer(String userName, Player player) {
		_players.put(userName, player);
		if (player.character.AttackItemId != null) {
			setItem(player.character.AttackItemId);
		} else {
			setItem(1);
		}
		if (player.character.DefenceItemId != null) {
			setItem(player.character.DefenceItemId);
		} else {
			setItem(2);
		}
	}

	@Override
	public Player getPlayer(String userName) {
		return _players.get(userName);
	}

	@Override
	public void removePlayer(String userName) {
		if (_players.get(userName).character.AttackItemId != null) {
			removeItem(_players.get(userName).character.AttackItemId);
		} else {
			removeItem(1);
		}
		if (_players.get(userName).character.DefenceItemId != null) {
			removeItem(_players.get(userName).character.DefenceItemId);
		} else {
			removeItem(2);
		}
		_players.remove(userName);
	}

	@Override
	public Set<Player> getReadyPlayersList() {
		return _readyPlayers;
	}

	@Override
	public boolean containsPlayer(String userName) {
		return _players.containsKey(userName);
	}

	@Override
	public ItemDAL getItem(int itemId) {
		return _items.get(itemId);
	}

	public static ICache getInstance() {
		return _cache;
	}

	private void setItem(int itemId) {
		if (_items.containsKey(itemId)) {
			_itemUseCounter.put(itemId, _itemUseCounter.get(itemId) + 1);
		} else {
			boolean success = downloadItem(itemId);
			if (success) {
				_itemUseCounter.put(itemId, 1);
			}
		}
	}

	private boolean downloadItem(int itemId) {
		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemId = itemId;
		ListDTO<ItemDAL> listDTO = _crud.read(itemDAL);
		if (listDTO.success && !listDTO.transferDataList.isEmpty()) {
			_items.put(listDTO.transferDataList.get(0).itemId, listDTO.transferDataList.get(0));
			return true;
		} else {
			_log.writeWarningMessage("Item No " + itemId + " wasn't downloaded from the database.", true,
					"Class: CacheImpl, method: private boolean downloadItem(int itemId).");
			return false;
		}
	}

	private void removeItem(int itemId) {
		if (_items.containsKey(itemId)) {
			_itemUseCounter.put(itemId, _itemUseCounter.get(itemId) - 1);
			if (_itemUseCounter.get(itemId) == 0) {
				_items.remove(itemId);
			}
		}
	}

}
