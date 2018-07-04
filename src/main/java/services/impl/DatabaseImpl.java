package services.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Singleton;

import services.IDatabase;

@Singleton
public class DatabaseImpl implements IDatabase {

	private Connection connection;

	/**
	 * Before changing a database please look at the database requirements and
	 * tables structure in a project documentation.
	 * 
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public Connection connect()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection("jdbc:mysql://88.223.54.41:3306/jfight", "CBjava2018",
				"Student_java2");
		System.out.println("Connection to database has been established.");
		return connection;
	}

	public void closeConnection() throws SQLException {
		if (connection != null && !connection.isClosed()) {
			connection.close();
			System.out.println("Database connection was closed.");
		}
	}

}
