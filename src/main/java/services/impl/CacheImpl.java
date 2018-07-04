package services.impl;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Singleton;

import services.ICache;

@Singleton
public class CacheImpl implements ICache {

	private Map<String, Object> objects;
	private Map<String, Boolean> userIsReady;
	private Map<String, Date> lastUserActivityTime;

	public CacheImpl() {
		objects = new ConcurrentHashMap<>();
		userIsReady = new ConcurrentHashMap<>();
		lastUserActivityTime = new ConcurrentHashMap<>();
	}

	@Override
	public void put(String key, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void remove(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUserIsRead(String userName, boolean userIsReady) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean getUserIsReady(String userName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean containsUser(String userName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Date getLastUserActivityTime(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

}
