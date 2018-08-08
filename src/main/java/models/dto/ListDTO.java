package models.dto;

import java.util.List;

public class ListDTO<T> extends DTO {

	public List<T> transferDataList;

	public ListDTO() {}

	public ListDTO(boolean inputSuccess, String inputMessage, List<T> inputTransferDataList) {
		success = inputSuccess;
		message = inputMessage;
		transferDataList = inputTransferDataList;
	}
}
