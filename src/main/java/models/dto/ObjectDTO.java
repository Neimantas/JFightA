package models.dto;

public class ObjectDTO<T> extends DTO {

	public T transferData;

	public ObjectDTO() {}

	public ObjectDTO(boolean inputSuccess, String inputMessage, T inputTransferData) {
		success = inputSuccess;
		message = inputMessage;
		transferData = inputTransferData;
	}
}
