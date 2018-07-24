package models.dto;

import models.dal.PlayerDAL;

public class PlayerDalDTO {
	public boolean _success;
	public String _message;
	public PlayerDAL _playerDal;

	public PlayerDalDTO() {
	}

	public PlayerDalDTO(boolean success, String message, PlayerDAL playerDal) {
		_success = success;
		_message = message;
		_playerDal = playerDal;
	}
}
