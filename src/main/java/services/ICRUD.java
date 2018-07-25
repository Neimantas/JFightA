package services;

import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;

public interface ICRUD {

	<T> ObjectDTO<Integer> create(T dal);
	<T> ListDTO<T> read(T dal);
	<T> DTO update(T dal);
	<T> DTO delete(T dal);

}
