package services.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import services.IDataBase;

public class DataBaseImpl implements IDataBase {
	
	Connection connection;

	public Connection connect() {
		try {
			connection = DriverManager.getConnection("jdbc:mysql://88.223.54.41:3306/jfight", "CBjava2018", "Student_java2");
			System.out.println("Works");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Don't work");
			e.printStackTrace();
		}
		return null;
	}

	public void closeConnection() {
		// TODO Auto-generated method stub
		
	}

}
