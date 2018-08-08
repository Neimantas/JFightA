package models.dto;

public class DTO {

	public boolean success;
	public String message;

	public DTO() {}

	public DTO(boolean inputSuccess, String inputMessage) {
		success = inputSuccess;
		message = inputMessage;
	}
}
