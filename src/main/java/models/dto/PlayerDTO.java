package models.dto;

import models.business.Player;

public class PlayerDTO {
	public boolean _success;
	public String _message;
	public Player _player;

	public PlayerDTO() {
	}

	public PlayerDTO(boolean success, String message, Player player) {
		_success = success;
		_message = message;
		_player = player;
	}
}
