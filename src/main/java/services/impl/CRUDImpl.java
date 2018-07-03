package services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import models.dal.ResultDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IDatabase;

@Singleton
public class CRUDImpl implements ICRUD {

	private IDatabase database;
	private Connection connection;
	private Statement statement;

	public CRUDImpl(DatabaseImpl databaseImpl) {
		database = databaseImpl;
	}

	/**
	 * Takes all values of a DAL and inserts as a row into database table. Returns
	 * DTO with DAL inside which represents created row in a database.
	 */
	@Override
	public <T> ObjectDTO<T> create(T dal) {
		try {
			ObjectDTO<T> objectDTO = new ObjectDTO<>();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();
			String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";

			String columnValues = "";

			for (int i = 0; i < dalClassFields.length; i++) {
				columnValues += (dalClassFields[i].getType() == Integer.class || dalClassFields[i].get(dal) == null
						? dalClassFields[i].get(dal) + ","
						: "\'" + dalClassFields[i].get(dal) + "\',");
			}

			columnValues = columnValues.substring(0, columnValues.length() - 1);
			String createQuery = "INSERT INTO " + tableName + " VALUES (" + columnValues + ");";

			setConnection();
			statement.executeUpdate(createQuery);

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
			returnDAL = read(returnDAL, false).transferDataList.get(0);

			objectDTO.transferData = returnDAL;
			objectDTO.success = true;
			objectDTO.message = "New " + tableName.replace("`", "") + " row created.";

			return objectDTO;
		} catch (SQLException e) {
			ObjectDTO<T> objectDTO = new ObjectDTO<>();
			objectDTO.message = "Database error. " + e.getMessage() + ".";
			return objectDTO;
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			ObjectDTO<T> objectDTO = new ObjectDTO<>();
			objectDTO.message = e.getMessage() + ".";
			return objectDTO;
		} finally {
			closeConnection();
		}
	}

	/**
	 * Makes a search in a database by input DAL and reads data. Returns DTO with
	 * list of DALs which represent all database table rows. If the first field of
	 * an input DAL (should represent PK or FK key in a database table) is not NULL
	 * and greater than 0 will be selected and returned only one row by input DAL
	 * Id. If there are no row with such Id in a database table, will be returned
	 * DTO with an empty list.
	 */
	@Override
	public <T> ListDTO<T> read(T dal) {
		return read(dal, true);
	}

	private <T> ListDTO<T> read(T dal, boolean setCloseConnection) {
		try {
			ListDTO<T> listDTO = new ListDTO<>();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();
			String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";

			Integer firstFieldValue = (Integer) dalClassFields[0].get(dal);

			String whereCondition = firstFieldValue == null || firstFieldValue < 1 ? ";"
					: " WHERE " + dalClassFields[0].getName() + " = " + firstFieldValue + ";";

			String readQuery = "SELECT * FROM " + tableName + whereCondition;

			ResultSet resultSet = statement.executeQuery(readQuery);

			List<T> dalList = new ArrayList<>();

			while (resultSet.next()) {

				T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();

				for (int i = 0; i < dalClassFields.length; i++) {

					Class<?> dalField = dalClassFields[i].getType();
					dalClassFields[i].set(returnDAL, (dalField.cast(resultSet.getObject(i + 1))));

				}

				dalList.add(returnDAL);

			}

			listDTO.transferDataList = dalList;
			listDTO.success = true;
			listDTO.message = !dalList.isEmpty() ? "Read successful."
					: (firstFieldValue != null && firstFieldValue > 0 ? "There are now data in a table with such Id."
							: "The database table is empty.");

			return listDTO;
		} catch (SQLException e) {
			ListDTO<T> listDTO = new ListDTO<>();
			listDTO.message = "Database error. " + e.getMessage() + ".";
			return listDTO;
		} catch (IllegalArgumentException | IllegalAccessException | ClassCastException | InstantiationException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			ListDTO<T> listDTO = new ListDTO<>();
			listDTO.message = e.getMessage() + ".";
			return listDTO;
		} finally {
			if (setCloseConnection) {
				closeConnection();
			}
		}
	}

	/**
	 * Reads results in a database result table by input userId. Returns DTO with
	 * List of ResultDALs inside, which represent only one user results.
	 */
	@Override
	public ListDTO<ResultDAL> readUserResults(int userId) {
		try {
			ListDTO<ResultDAL> listDTO = new ListDTO<>();
			if (userId < 1) {
				listDTO.message = "Wrong user Id.";
				return listDTO;
			}

			String readQuery = "SELECT * FROM `Result` WHERE WinUserId = " + userId + " OR LossUserId = " + userId
					+ " OR TieUser1Id = " + userId + " OR TieUser2Id = " + userId + ";";

			setConnection();
			ResultSet resultSet = statement.executeQuery(readQuery);

			List<ResultDAL> resultDALLis = new ArrayList<>();

			while (resultSet.next()) {

				ResultDAL resultDAL = new ResultDAL();

				resultDAL.fightId = (Integer) resultSet.getObject("FightId");
				resultDAL.winUserId = (Integer) resultSet.getObject("WinUserId");
				resultDAL.lossUserId = (Integer) resultSet.getObject("LossUserId");
				resultDAL.tieUser1Id = (Integer) resultSet.getObject("TieUser1Id");
				resultDAL.tieUser2Id = (Integer) resultSet.getObject("TieUser2Id");

				resultDALLis.add(resultDAL);

			}

			listDTO.transferDataList = resultDALLis;
			listDTO.success = true;
			listDTO.message = !resultDALLis.isEmpty() ? "Read successful."
					: "There are now data in a result table with such Id (" + userId + ").";

			return listDTO;
		} catch (SQLException e) {
			ListDTO<ResultDAL> listDTO = new ListDTO<>();
			listDTO.message = "Database error. " + e.getMessage() + ".";
			return listDTO;
		} finally {
			closeConnection();
		}
	}

	/**
	 * Updates table row values by an input DAL. Row updates by the first field
	 * value of a DAL. This field should represent primary or foreign key on a table
	 * and should not be a null or less than 1. All fields on a table row overwrites
	 * by a values on an input DAL.
	 */
	@Override
	public <T> DTO update(T dal) {
		try {
			DTO dto = new DTO();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();

			Integer firstFieldValue = (Integer) dalClassFields[0].get(dal);

			if (firstFieldValue == null || firstFieldValue < 1) {
				dto.message = "Wrong input DAL first field value (should be not null and greater than 0).";
				return dto;
			}

			setConnection();

			ListDTO<T> readDTO = read(dal, false);

			if (readDTO.transferDataList.isEmpty()) {
				dto.message = "Update failed. There are now row in a table with such Id (" + firstFieldValue + ").";
				return dto;
			}

			String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";

			String columnValues = "";

			for (int i = 1; i < dalClassFields.length; i++) {
				columnValues += dalClassFields[i].getName() + " = "
						+ (dalClassFields[i].getType() == Integer.class || dalClassFields[i].get(dal) == null
								? dalClassFields[i].get(dal) + " + "
								: "\'" + dalClassFields[i].get(dal) + "\', ");
			}

			columnValues = columnValues.substring(0, columnValues.length() - 2);
			String whereCondition = " WHERE " + dalClassFields[0].getName() + " = " + dalClassFields[0].get(dal) + ";";
			String updateQuery = "UPDATE " + tableName + " SET " + columnValues + whereCondition;

			statement.executeUpdate(updateQuery);

			dto.success = true;
			dto.message = "Update successful.";

			return dto;
		} catch (SQLException e) {
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			DTO dto = new DTO();
			dto.message = e.getMessage() + ".";
			return dto;
		} finally {
			closeConnection();
		}
	}

	/**
	 * Deletes row from the table by an input DAL. Row deletes by the first field
	 * value of a DAL. This field should represent primary or foreign key on a table
	 * and should not be a null or less than 1.
	 */
	@Override
	public <T> DTO delete(T dal) {
		try {
			DTO dto = new DTO();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();

			Integer firstFieldValue = (Integer) dalClassFields[0].get(dal);

			if (firstFieldValue == null || firstFieldValue < 1) {
				dto.message = "Wrong input DAL first field value (should be not null and greater than 0).";
				return dto;
			}

			setConnection();

			ListDTO<T> readDTO = read(dal, false);

			if (readDTO.transferDataList.isEmpty()) {
				dto.message = "Delete failed. There are now row in a table with such Id (" + firstFieldValue + ").";
				return dto;
			}

			String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";
			String columnValue = dalClassFields[0].getName() + " = " + dalClassFields[0].get(dal) + ";";
			String deleteQuery = "DELETE FROM " + tableName + " WHERE " + columnValue;

			statement.executeUpdate(deleteQuery);

			dto.success = true;
			dto.message = "Row deleted successfully.";

			return dto;
		} catch (SQLException e) {
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			DTO dto = new DTO();
			dto.message = e.getMessage() + ".";
			return dto;
		} finally {
			closeConnection();
		}
	}

	/**
	 * Upload an image from the Web image path to database.
	 */
	@Override
	public DTO uploadImage(int userId, String fileName) {
		try (Connection imageConnection = database.connect();) {

			String imageFormat = fileName.substring(fileName.lastIndexOf('.'));

			File file = new File("src\\main\\webapp\\resources\\images\\charecters\\" + fileName);
			FileInputStream fileInputStream = new FileInputStream(file);

			PreparedStatement preparedStatement = imageConnection
					.prepareStatement("INSERT INTO `image` (UserId, Image, ImageFormat) VALUES (?, ?, ?)");
			preparedStatement.setInt(1, userId);
			preparedStatement.setBinaryStream(2, fileInputStream, file.length());
			preparedStatement.setString(3, imageFormat);

			preparedStatement.executeUpdate();

			preparedStatement.close();
			fileInputStream.close();

			DTO dto = new DTO();
			dto.success = true;
			dto.message = "File uploaded to database successfully.";

			System.out.println("Database connection was closed.");
			return dto;
		} catch (SQLException e) {
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} catch (IOException e) {
			DTO dto = new DTO();
			dto.message = e.getMessage() + ".";
			return dto;
		}
	}

	/**
	 * Downloads image from database to the Web image path.
	 */
	@Override
	public DTO getImage(int userId) {
		try (Connection imageConnection = database.connect();) {

			DTO dto = new DTO();

			PreparedStatement preparedStatement = imageConnection
					.prepareStatement("SELECT * FROM `image` WHERE UserId = " + userId + ";");
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {

				byte[] bytes;
				Blob blob;

				String imageFormat = resultSet.getString(3);

				File file = new File("src\\main\\webapp\\resources\\images\\charecters\\" + userId + imageFormat);
				FileOutputStream fileOutputStream = new FileOutputStream(file);

				blob = resultSet.getBlob("image");
				bytes = blob.getBytes(1, (int) blob.length());
				fileOutputStream.write(bytes);

				fileOutputStream.close();

			} else {
				preparedStatement.close();
				dto.message = "There are now image in a database with such Id.";
				return dto;
			}

			preparedStatement.close();

			dto.success = true;
			dto.message = "File downloaded from the database successfully.";

			System.out.println("Database connection was closed.");
			return dto;
		} catch (SQLException e) {
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} catch (IOException e) {
			DTO dto = new DTO();
			dto.message = e.getMessage() + ".";
			return dto;
		}
	}

	/**
	 * Deletes image from the database by userId.
	 */
	@Override
	public DTO deleteImage(int userId) {
		try {
			DTO dto = new DTO();

			setConnection();
			ResultSet resultSet = statement.executeQuery("SELECT UserId FROM `image` WHERE UserId = " + userId + ";");

			if (resultSet.next()) {

				statement.executeUpdate("DELETE FROM `image` WHERE UserId = " + userId + ";");

			} else {
				dto.message = "There are now image in a database with such Id.";
				return dto;
			}

			dto.success = true;
			dto.message = "Image deleted successfully.";

			return dto;
		} catch (SQLException e) {
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} finally {
			closeConnection();
		}
	}

	private void setConnection() throws SQLException {
		if (connection == null || connection.isClosed()) {
			connection = database.connect();
			statement = connection.createStatement();
		}
	}

	private void closeConnection() {
		try {
			if (statement != null) {
				statement.close();
			}
			database.closeConnection();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}