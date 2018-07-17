package services;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabase {

	Connection connect() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException;
	void closeConnection() throws SQLException;

}
