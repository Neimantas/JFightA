package services.impl;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Models.dto.DTOmsg;
import Models.dto.DTO;
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
	public <T> DTOmsg create(T dal) {
		
		DTOmsg dtoMsg = new DTOmsg();
		
		Class<?> dalClass = dal.getClass();
		Field[] dalClassFields = dalClass.getFields();
		String tableName = dalClass.getSimpleName().replace("DAL", "");
		
		String columnValues = "";
		
		for (int i = 0; i < dalClassFields.length; i++) {
			
			
			
		}
		
		return dtoMsg;
	}

	@Override
	public <T> DTO<T> read(T dal) {

		try {

			DTO<T> dto = new DTO<>();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();
			String tableName = dalClass.getSimpleName().replace("DAL", "");

			String readQuerry = "SELECT * FROM " + tableName + ";";

			// setting connection in a try block,
			// so it will be closed automatically (since Java7)
			setConnection();
			ResultSet resultSet = statement.executeQuery(readQuerry);

			List<T> dalList = new ArrayList<>();

			while (resultSet.next()) {

				T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();

				for (int i = 0; i < dalClassFields.length; i++) {

					Class<?> dalField = dalClassFields[i].getType();
					dalClassFields[i].set(returnDAL, resultSet.getObject(i + 1, dalField));

				}

				dalList.add(returnDAL);

			}

			dto.transferData = dalList;
			dto.success = true;
			dto.message = "Success";

			System.out.println("Database connection was closed.");
			return dto;
			
		} catch (SQLException | IllegalArgumentException | IllegalAccessException | ClassCastException
				| InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {
			DTO<T> dtoExt = new DTO<>();
			dtoExt.message = e.getMessage();
			return dtoExt;
		}

	}

	@Override
	public <T> DTOmsg update(T dal) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> DTOmsg delete(T dal) {
		// TODO Auto-generated method stub
		return null;
	}

	private void setConnection() throws SQLException {
		connection = database.connect();
		statement = connection.createStatement();
	}

}
