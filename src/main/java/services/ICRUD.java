package services;

import Models.dto.CreateTableDTO;
import Models.dto.DeleteTableDTO;
import Models.dto.ReadTableDTO;
import Models.dto.UpdateTableDTO;

public interface ICRUD {

	public CreateTableDTO create(Object dal);

	public ReadTableDTO read(Object dal);

	public UpdateTableDTO update(Object dal);

	public DeleteTableDTO delete(Object dal);

}
