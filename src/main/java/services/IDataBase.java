package services;

import java.sql.Connection;

public interface IDataBase {

	Connection connect();
	void closeConnection();

}
