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
import Models.dto.ListDTO;
import Models.dto.ObjectDTO;
import services.ICRUD;
import services.IDatabase;

public class CRUDImpl implements ICRUD {

	IDatabase database;
	Connection connection;
	Statement statement;

	public CRUDImpl(DatabaseImpl databaseImpl) {
		database = databaseImpl;
	}

	/**
	 * Returns DTO with DAL inside which represents created row in a database
	 */
	@Override
	public <T> ObjectDTO<T> create(T dal) {
		try {
			ObjectDTO<T> objectDTO = new ObjectDTO<>();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();
			String tableName = dalClass.getSimpleName().replace("DAL", "");

			String columnValues = "";

			for (int i = 0; i < dalClassFields.length; i++) {
				columnValues += (dalClassFields[i].getType() == Integer.class || dalClassFields[i].get(dal) == null
						? dalClassFields[i].get(dal) + ","
						: "\'" + dalClassFields[i].get(dal) + "\',");
			}

			columnValues = columnValues.substring(0, columnValues.length() - 1);
			String query = "INSERT INTO " + tableName + " VALUES (" + columnValues + ");";

			setConnection();
			statement.executeUpdate(query);

			T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();
			Integer dalId;

			if (dalClassFields[0].get(dal) == null) {
				ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
				resultSet.next();
				dalId = resultSet.getInt(1);
			} else {
				dalId = (Integer) dalClassFields[0].get(dal);
			}

			dalClassFields[0].set(returnDAL, dalId);
			returnDAL = read(returnDAL).transferDataList.get(0);

			objectDTO.transferData = returnDAL;
			objectDTO.success = true;
			objectDTO.message = "New " + tableName + " row created.";

			return objectDTO;
		} catch (IllegalArgumentException | IllegalAccessException | SQLException | InstantiationException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			ObjectDTO<T> objectDTO = new ObjectDTO<>();
			objectDTO.message = "Database error. " + e.getMessage();
			return objectDTO;
		}
	}

	/**
	 * Returns DTO with list of DALs which represents all database table rows. If
	 * the first field of input DAL (should represent PK or FK key in a database
	 * table) is not NULL will be selected and returned only one row by input DAL
	 * ID.
	 */
	@Override
	public <T> ListDTO<T> read(T dal) {
		try {
			ListDTO<T> listDTO = new ListDTO<>();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();
			String tableName = dalClass.getSimpleName().replace("DAL", "");

			String whereCondition = dalClassFields[0].get(dal) == null ? ";"
					: " WHERE " + dalClassFields[0].getName() + " = " + dalClassFields[0].get(dal) + ";";

			String readQuery = "SELECT * FROM " + tableName + whereCondition;

			setConnection();
			ResultSet resultSet = statement.executeQuery(readQuery);

			List<T> dalList = new ArrayList<>();

			while (resultSet.next()) {

				T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();

				for (int i = 0; i < dalClassFields.length; i++) {

					Class<?> dalField = dalClassFields[i].getType();
					dalClassFields[i].set(returnDAL, resultSet.getObject(i + 1, dalField));

				}

				dalList.add(returnDAL);

			}

			listDTO.transferDataList = dalList;
			listDTO.success = true;
			listDTO.message = "Success";

			System.out.println("Database connection was closed.");
			return listDTO;
			// connection setted in a try block so it closes autmatically (since Java7)
		} catch (SQLException | IllegalArgumentException | IllegalAccessException | ClassCastException
				| InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException
				| ClassNotFoundException e) {
			ListDTO<T> listDTO = new ListDTO<>();
			listDTO.message = "Database error. " + e.getMessage();
			return listDTO;
		}
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
		if (connection == null || connection.isClosed()) {
			connection = database.connect();
			statement = connection.createStatement();
		}
	}

}
