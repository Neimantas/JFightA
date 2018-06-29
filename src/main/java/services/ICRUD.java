package services;

import Models.dto.DTOmsg;
import Models.dto.DTO;

public interface ICRUD {

	public <T> DTOmsg create(T dal);

	public <T> DTO<T> read(T dal);

	public <T> DTOmsg update(T dal);

	public <T> DTOmsg delete(T dal);

}
