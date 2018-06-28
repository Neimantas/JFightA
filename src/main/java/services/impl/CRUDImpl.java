package services.impl;

import Models.dto.CreateTableDTO;
import Models.dto.DeleteTableDTO;
import Models.dto.ReadTableDTO;
import Models.dto.UpdateTableDTO;
import services.ICRUD;
import services.IDatabase;

public class CRUDImpl implements ICRUD {
	
	IDatabase database;
	
	public CRUDImpl(DatabaseImpl databaseImpl) {
		database = databaseImpl;
	}

	@Override
	public CreateTableDTO create(Object dal) {
		CreateTableDTO createTableDTO = new CreateTableDTO();
		
		
		
		return createTableDTO;
	}

	@Override
	public ReadTableDTO read(Object dal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UpdateTableDTO update(Object dal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteTableDTO delete(Object dal) {
		// TODO Auto-generated method stub
		return null;
	}

}
