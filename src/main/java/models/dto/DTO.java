package models.dto;

public class DTO {

	public boolean success;
	public String message;

	public DTO() {
	};

	public DTO(boolean _success, String _message) {
		success = _success;
		message = _message;
	};
}
