package models.business;

import java.util.Date;

import models.constant.UserStatus;
import services.impl.CacheImpl;

public class Player {

	private User _user;
	private Character _character;
	private UserStatus _userStatus;
	private Date _lastActivityTime;

	public Player() {
		_userStatus = UserStatus.NOT_READY;
		_lastActivityTime = new Date();
	}

	public Player(User loggedInUser, Character userCharacter) {
		this();
		_user = loggedInUser;
		_character = userCharacter;
	}

	public User getUser() {
		return _user;
	}

	public void setUser(User loggedInUser) {
		_user = loggedInUser;
	}

	public Character getCharacter() {
		return _character;
	}

	public void setCharacter(Character userCharacter) {
		_character = userCharacter;
	}

	public UserStatus getUserStatus() {
		return _userStatus;
	}

	public void setUserStatus(UserStatus newUserStatus) {
		_userStatus = newUserStatus;
		_lastActivityTime = new Date();
		if (newUserStatus == UserStatus.READY) {
			CacheImpl.getInstance().getReadyUsersList().add(this);
		} else {
			CacheImpl.getInstance().getReadyUsersList().remove(this);
		}
	}

	public Date getLastActivityTime() {
		return _lastActivityTime;
	}

}