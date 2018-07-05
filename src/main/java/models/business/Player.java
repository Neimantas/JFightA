package models.business;

import java.util.Date;

import models.constant.UserStatus;
import services.impl.CacheImpl;

public class Player {

	private User user;
	private Character character;
	private UserStatus userStatus;
	private Date lastActivityTime;

	public Player() {
		userStatus = UserStatus.NOT_READY;
		lastActivityTime = new Date();
	}

	public Player(User loggedInUser, Character userCharacter) {
		this();
		user = loggedInUser;
		character = userCharacter;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User loggedInUser) {
		user = loggedInUser;
	}

	public Character getCharacter() {
		return character;
	}

	public void setCharacter(Character userCharacter) {
		character = userCharacter;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus newUserStatus) {
		userStatus = newUserStatus;
		lastActivityTime = new Date();
		if (newUserStatus == UserStatus.READY) {
			CacheImpl.cache.getReadyUsersList().add(this);
		} else {
			CacheImpl.cache.getReadyUsersList().remove(this);
		}
	}

	public Date getLastActivityTime() {
		return lastActivityTime;
	}

}