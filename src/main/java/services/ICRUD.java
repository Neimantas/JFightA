package services;

import Models.dto.DTO;
import Models.dto.DTOext;

public interface ICRUD {

	public <T> DTO create(T dal);

	public <T> DTOext<T> read(T dal);

	public <T> DTO update(T dal);

	public <T> DTO delete(T dal);

}
