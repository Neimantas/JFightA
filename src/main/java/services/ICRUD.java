package services;

import Models.dto.DTO;
import Models.dto.ListDTO;
import Models.dto.ObjectDTO;

public interface ICRUD {

	public <T> ObjectDTO<T> create(T dal);

	public <T> ListDTO<T> read(T dal);

	public <T> DTO update(T dal);

	public <T> DTO delete(T dal);

}
