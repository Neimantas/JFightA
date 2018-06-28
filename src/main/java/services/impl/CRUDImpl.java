package services.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Models.dto.DTO;
import Models.dto.DTOext;
import services.ICRUD;
import services.IDatabase;

public class CRUDImpl implements ICRUD {

	IDatabase database;
	Connection connection;
	Statement statement;

	public CRUDImpl(DatabaseImpl databaseImpl) {
		database = databaseImpl;
	}

	@Override
	public <T> DTO create(T dal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> DTOext<T> read(T dal) {

		DTOext<T> dtoExt = new DTOext<>();

		Class<?> dalClass = dal.getClass();
		Field[] dalClassFields = dalClass.getFields();
		String tableName = dalClass.getSimpleName().replace("DAL", "");

		String readQuerry = "SELECT * FROM " + tableName + ";";

		try { // setting connection in a try block,
				// so it will be closed automatically (since Java7)
			setConnection();
			ResultSet resultSet = statement.executeQuery(readQuerry);

			List<T> dalList = new ArrayList<>();

			while (resultSet.next()) {

				T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();

				for (int i = 0; i < dalClassFields.length; i++) {

					Class<?> dalField = dalClassFields[i].getType();
					dalClassFields[i].set(returnDAL, dalField.cast(resultSet.getObject(i + 1)));

				}

				dalList.add(returnDAL);

			}

			dtoExt.transferData = dalList;
			dtoExt.success = true;
			dtoExt.message = "Success";

			System.out.println("Database connection was closed.");
		} catch (SQLException | IllegalArgumentException | IllegalAccessException | ClassCastException
				| InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {
			dtoExt.message = e.getMessage();
		}

		return dtoExt;
	}

	@Override
	public <T> DTO update(T dal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> DTO delete(T dal) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setConnection() throws SQLException {
		connection = database.connect();
		statement = connection.createStatement();
	}

}
