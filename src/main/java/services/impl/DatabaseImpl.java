package services.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import services.IDatabase;

public class DatabaseImpl implements IDatabase {
	private static IDatabase _database = new DatabaseImpl();

	private Connection _connection;

	public DatabaseImpl() {
	}

	/**
	 * Before changing a database please look at the database requirements and
	 * tables structure in a project documentation.
	 */
	public Connection connect()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		_connection = DriverManager.getConnection("jdbc:mysql://88.223.54.41:3306/jfight", "CBjava2018",
				"Student_java2");
		System.out.println("Connection to database has been established.");
		return _connection;
	}

	public void closeConnection() throws SQLException {
		if (_connection != null && !_connection.isClosed()) {
			_connection.close();
			System.out.println("Database connection was closed.");
		}
	}

	public static IDatabase getInstance() {
		return _database;
	}

}
