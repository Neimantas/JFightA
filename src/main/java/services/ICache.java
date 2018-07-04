package services;

import java.util.Date;

public interface ICache {

	public void put(String key, Object data);

	public Object get(String key);

	public void remove(String key);

	public void setUserIsRead(String userName, boolean userIsReady);

	public Date getLastUserActivityTime(String userName);

	public boolean getUserIsReady(String userName);

	public boolean containsUser(String userName);

}
