package services;

import models.dal.ResultDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;

public interface ICRUD {

	public <T> ObjectDTO<T> create(T dal);

	public <T> ListDTO<T> read(T dal);

	public ListDTO<ResultDAL> readUserResults(int userId);

	public <T> DTO update(T dal);

	public <T> DTO delete(T dal);

	public DTO uploadImage(int userId, String fileName);

	public DTO getImage(int userId);

	public DTO deleteImage(int userId);

}
