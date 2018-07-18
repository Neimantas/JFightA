package services.impl;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.constant.ItemType;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IDatabase;
import services.ILog;

public class CRUDImpl implements ICRUD {
	private static ICRUD _crud = new CRUDImpl();

	private IDatabase _database;
	private Connection _connection;
	private PreparedStatement _preparedStatement;
	private ILog _log;

	private CRUDImpl() {
		_database = DatabaseImpl.getInstance();
		_log = LogImpl.getInstance();
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

			String createQuery = createCreateQuery(dal, dalClassFields, tableName);

			setConnection();
			_preparedStatement = _connection.prepareStatement(createQuery);
			if (tableName.equalsIgnoreCase("`Image`")) {
				ImageDAL imageDAL = (ImageDAL) dal;
				_preparedStatement.setBinaryStream(1, imageDAL.image);
			} else if (tableName.equalsIgnoreCase("`Item`")) {
				ItemDAL itemDAL = (ItemDAL) dal;
				_preparedStatement.setBinaryStream(1, itemDAL.itemImage);
			}
			_preparedStatement.executeUpdate();

			T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();
			Integer dalId;

			if (dalClassFields[0].get(dal) == null) {
				Statement statement = _connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
				resultSet.next();
				dalId = resultSet.getInt(1);
				statement.close();
			} else {
				dalId = (Integer) dalClassFields[0].get(dal);
			}

			dalClassFields[0].set(returnDAL, dalId);
			_preparedStatement.close();
			returnDAL = read(returnDAL, false).transferDataList.get(0);

			objectDTO.transferData = returnDAL;
			objectDTO.success = true;
			objectDTO.message = "New " + tableName.replace("`", "") + " row created.";

			return objectDTO;
		} catch (SQLException e) {
			_log.writeErrorMessage(e, true);
			ObjectDTO<T> objectDTO = new ObjectDTO<>();
			objectDTO.message = "Database error. " + e.getMessage() + ".";
			return objectDTO;
		} catch (IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			_log.writeErrorMessage(e, true);
			ObjectDTO<T> objectDTO = new ObjectDTO<>();
			objectDTO.message = e.getMessage() + ".";
			return objectDTO;
		} finally {
			closeConnection();
		}
	}

	/**
	 * Makes a search in a database by input DAL and reads data. Returns DTO with
	 * list of DALs which represent database table rows. If input DAL fields is not
	 * NULL will be selected and returned only rows that contains columns with such
	 * values. If there are no rows with such values in a database table or database
	 * table is empty, will be returned DTO with an empty list.
	 */
	@Override
	public <T> ListDTO<T> read(T dal) {
		return read(dal, true);
	}

	private <T> ListDTO<T> read(T dal, boolean setConnection, String... checkQuery) {
		try {
			ListDTO<T> listDTO = new ListDTO<>();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();

			String readQuery = checkQuery.length == 0 ? createReadQuery(dal, dalClass, dalClassFields) : checkQuery[0];

			Boolean hasCondition = readQuery.contains("WHERE");

			if (setConnection) {
				setConnection();
			}

			_preparedStatement = _connection.prepareStatement(readQuery);
			ResultSet resultSet = _preparedStatement.executeQuery();

			List<T> dalList = new ArrayList<>();

			while (resultSet.next()) {

				T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();

				boolean itemDALNonStandartFieldsSetted = false;
				for (int j = 0; j < dalClassFields.length; j++) {

					Class<?> dalField = dalClassFields[j].getType();
					if (dalField != InputStream.class && dalField != ItemType.class) {
						dalClassFields[j].set(returnDAL, (dalField.cast(resultSet.getObject(j + 1))));
					} else {
						if (dalClass == ImageDAL.class) {
							ImageDAL imageDAL = (ImageDAL) returnDAL;
							imageDAL.image = resultSet.getBinaryStream("Image");
							returnDAL = (T) imageDAL;
						} else if (dalClass == ItemDAL.class) {
							if (!itemDALNonStandartFieldsSetted) {
								ItemDAL itemDAL = (ItemDAL) returnDAL;
								itemDAL.itemImage = resultSet.getBinaryStream("ItemImage");
								itemDAL.itemType = ItemType.getByItemTypeTitle(resultSet.getString("ItemType"));
								returnDAL = (T) itemDAL;
								itemDALNonStandartFieldsSetted = true;
							}
						}

					}
				}

				dalList.add(returnDAL);

			}

			listDTO.transferDataList = dalList;
			listDTO.success = true;
			listDTO.message = !dalList.isEmpty() ? "Read successful."
					: (hasCondition == true ? "There are now data in a table with such values."
							: "The database table is empty.");

			return listDTO;
		} catch (SQLException e) {
			_log.writeErrorMessage(e, true);
			ListDTO<T> listDTO = new ListDTO<>();
			listDTO.message = "Database error. " + e.getMessage() + ".";
			return listDTO;
		} catch (IllegalArgumentException | IllegalAccessException | ClassCastException | InstantiationException
				| InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			_log.writeErrorMessage(e, true);
			ListDTO<T> listDTO = new ListDTO<>();
			listDTO.message = e.getMessage() + ".";
			return listDTO;
		} finally {
			if (setConnection) {
				closeConnection();
			}
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
				_log.writeWarningMessage("CRUD update failed. " + dto.message, true, "Input object: " + dalClass.getSimpleName() + ".");
				return dto;
			}

			setConnection();

			String checkQuery = createCheckQuery(dal, dalClass, dalClassFields);
			ListDTO<T> readDTO = read(dal, false, checkQuery);

			if (readDTO.transferDataList.isEmpty()) {
				dto.message = "CRUD update failed. There are now row in a table with such Id (" + firstFieldValue + ").";
				_log.writeWarningMessage(dto.message, true, "Input object: " + dalClass.getSimpleName() + ".");
				return dto;
			}

			String updateQuery = createUpdateQuery(dal, dalClass, dalClassFields);

			_preparedStatement.close();
			_preparedStatement = _connection.prepareStatement(updateQuery);
			if (dalClass == ImageDAL.class) {
				ImageDAL imageDAL = (ImageDAL) dal;
				_preparedStatement.setBinaryStream(1, imageDAL.image);
			} else if (dalClass == ItemDAL.class) {
				ItemDAL itemDAL = (ItemDAL) dal;
				_preparedStatement.setBinaryStream(1, itemDAL.itemImage);
			}
			_preparedStatement.executeUpdate();

			dto.success = true;
			dto.message = "Update successful.";

			return dto;
		} catch (SQLException e) {
			_log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} catch (InstantiationException | ClassNotFoundException | IllegalArgumentException
				| IllegalAccessException e) {
			_log.writeErrorMessage(e, true);
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
				_log.writeWarningMessage("CRUD delete failed. " + dto.message, true, "Input object: " + dalClass.getSimpleName() + ".");
				return dto;
			}

			setConnection();

			String checkQuery = createCheckQuery(dal, dalClass, dalClassFields);
			ListDTO<T> readDTO = read(dal, false, checkQuery);

			if (readDTO.transferDataList.isEmpty()) {
				dto.message = "CRUD delete failed. There are now row in a table with such Id (" + firstFieldValue + ").";
				_log.writeWarningMessage(dto.message, true, "Input object: " + dalClass.getSimpleName() + ".");
				return dto;
			}

			String deleteQuery = createDeleteQuery(dal, dalClass, dalClassFields);

			_preparedStatement.close();
			_preparedStatement = _connection.prepareStatement(deleteQuery);
			_preparedStatement.executeUpdate();

			dto.success = true;
			dto.message = "Row deleted successfully.";

			return dto;
		} catch (SQLException e) {
			_log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} catch (InstantiationException | ClassNotFoundException | IllegalArgumentException
				| IllegalAccessException e) {
			_log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = e.getMessage() + ".";
			return dto;
		} finally {
			closeConnection();
		}
	}

	public static ICRUD getInstance() {
		return _crud;
	}

	private void setConnection()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (_connection == null || _connection.isClosed()) {
			_connection = _database.connect();
		}
	}

	private void closeConnection() {
		try {
			if (_preparedStatement != null) {
				_preparedStatement.close();
			}
			_database.closeConnection();
		} catch (SQLException e) {
			_log.writeErrorMessage(e, true);
		}
	}

	private <T> String createCreateQuery(T dal, Field[] dalClassFields, String tableName)
			throws IllegalAccessException {
		String columnValues = "";

		for (int i = 0; i < dalClassFields.length; i++) {
			if (dalClassFields[i].getType() != InputStream.class) {
				columnValues += (dalClassFields[i].getType() == Integer.class || dalClassFields[i].get(dal) == null
						? dalClassFields[i].get(dal) + ", "
						: "\'" + dalClassFields[i].get(dal) + "\', ");
			} else {
				columnValues += "?, ";
			}
		}

		columnValues = columnValues.substring(0, columnValues.length() - 2);
		String createQuery = "INSERT INTO " + tableName + " VALUES (" + columnValues + ");";
		System.out.println(createQuery + "\n");
		return createQuery;
	}

	private <T> String createReadQuery(T dal, Class<?> dalClass, Field[] dalClassFields)
			throws IllegalArgumentException, IllegalAccessException {

		String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";

		if (tableName.equalsIgnoreCase("`Result`")
				&& (dalClassFields[1].get(dal) != null || dalClassFields[2].get(dal) != null
						|| dalClassFields[3].get(dal) != null || dalClassFields[4].get(dal) != null)) {
			return createResultTableQuery(dal, dalClassFields);
		}

		String readQuery = "";
		String whereCondition = "";
		Boolean isCondition = false;
		for (int i = 0; i < dalClassFields.length; i++) {

			if (dalClassFields[i].get(dal) != null) {

				Class<?> dalField = dalClassFields[i].getType();

				if (dalField != InputStream.class) {
					whereCondition += (!isCondition ? " WHERE " : " AND ") + dalClassFields[i].getName() + " = "
							+ (dalField == Integer.class ? "" : "\'") + dalClassFields[i].get(dal)
							+ (dalField == Integer.class ? "" : "\'");

					if (!isCondition) {
						isCondition = true;
					}
				}
			}
		}

		whereCondition += ";";

		readQuery = "SELECT * FROM " + tableName + whereCondition;
		System.out.println(readQuery + "\n");
		return readQuery;
	}

	/**
	 * If FightId is not null, will be created read query for a row with such
	 * FightId. Otherwise the first found userId will be taken and will be created
	 * read query for all result with such userId (no matter win or lose).
	 */
	private <T> String createResultTableQuery(T dal, Field[] dalClassFields)
			throws IllegalArgumentException, IllegalAccessException {
		String readQuery = "SELECT * FROM `Result` WHERE ";

		if (dalClassFields[0].get(dal) != null) {
			readQuery += "FightId = " + dalClassFields[0].get(dal) + ";";
		} else {

			for (int i = 1; i < dalClassFields.length; i++) {

				if (dalClassFields[i].get(dal) != null) {
					int userId = (Integer) dalClassFields[i].get(dal);
					readQuery += "WinUserId = " + userId + " OR LossUserId = " + userId + " OR TieUser1Id = " + userId
							+ " OR TieUser2Id = " + userId + ";";
					break;
				}
			}
		}
		System.out.println(readQuery + "\n");
		return readQuery;
	}

	private <T> String createUpdateQuery(T dal, Class<?> dalClass, Field[] dalClassFields)
			throws IllegalAccessException {
		String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";

		String columnValues = "";

		for (int i = 1; i < dalClassFields.length; i++) {

			if (dalClassFields[i].getType() != InputStream.class) {
				columnValues += dalClassFields[i].getName() + " = "
						+ (dalClassFields[i].getType() == Integer.class || dalClassFields[i].get(dal) == null
								? dalClassFields[i].get(dal) + ", "
								: "\'" + dalClassFields[i].get(dal) + "\', ");
			} else {
				columnValues += dalClassFields[i].getName() + " = " + "?, ";
			}
		}

		columnValues = columnValues.substring(0, columnValues.length() - 2);
		String whereCondition = " WHERE " + dalClassFields[0].getName() + " = " + dalClassFields[0].get(dal) + ";";
		String updateQuery = "UPDATE " + tableName + " SET " + columnValues + whereCondition;
		System.out.println(updateQuery + "\n");
		return updateQuery;
	}

	private <T> String createDeleteQuery(T dal, Class<?> dalClass, Field[] dalClassFields)
			throws IllegalAccessException {
		String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";
		String columnValue = dalClassFields[0].getName() + " = " + dalClassFields[0].get(dal) + ";";
		String deleteQuery = "DELETE FROM " + tableName + " WHERE " + columnValue;
		System.out.println(deleteQuery + "\n");
		return deleteQuery;
	}

	/**
	 * Checks if row with such Id (PK or FK) exists.
	 */
	private <T> String createCheckQuery(T dal, Class<?> dalClass, Field[] dalClassFields)
			throws IllegalArgumentException, IllegalAccessException {
		String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";
		String whereCondition = " WHERE " + dalClassFields[0].getName() + " = " + dalClassFields[0].get(dal) + ";";
		String checkQuery = "SELECT * FROM " + tableName + whereCondition;
		System.out.println(checkQuery + "\n");
		return checkQuery;
	}

}
