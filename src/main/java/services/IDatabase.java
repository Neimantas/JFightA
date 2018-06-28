package services;

import java.sql.Connection;

public interface IDatabase {

	Connection connect();
	void closeConnection();

}
