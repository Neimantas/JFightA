package services.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import services.IDatabase;

public class DatabaseImpl implements IDatabase {

	private Connection _connection;

	/**
	 * Before changing a database please look at the database requirements and
	 * tables structure in a project documentation.
	 */
	public Connection connect()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		_connection = DriverManager.getConnection("jdbc:mysql://88.223.54.41:3306/jfight", "CBjava2018",
				"Student_java2");
		return _connection;
	}

	public void closeConnection() throws SQLException {
		if (_connection != null && !_connection.isClosed()) {
			_connection.close();
		}
	}

}
