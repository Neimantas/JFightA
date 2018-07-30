package services.impl;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import models.constant.Error;
import models.constant.ItemType;
import models.constant.Success;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IDatabase;
import services.ILog;

public class CRUDImpl implements ICRUD {

	private ILog _log;

	public CRUDImpl() {
		_log = LogImpl.getInstance();
	}

	/**
	 * Takes all values of a DAL and inserts as a row into database table. Returns
	 * DTO with Integer inside which represents created row PK or FK Id in a
	 * database.
	 */
	@Override
	public <T> ObjectDTO<Integer> create(T dal) {
		try (Connection connection = setDatabaseConnection()) {
			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();
			String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";

			String createQuery = createCreateQuery(dal, dalClassFields, tableName);

			PreparedStatement preparedStatement = connection.prepareStatement(createQuery);
			if (tableName.equalsIgnoreCase("`Image`")) {
				ImageDAL imageDAL = (ImageDAL) dal;
				preparedStatement.setBytes(1, imageDAL.image);
			} else if (tableName.equalsIgnoreCase("`Item`")) {
				ItemDAL itemDAL = (ItemDAL) dal;
				preparedStatement.setBytes(1, itemDAL.itemImage);
			}
			preparedStatement.executeUpdate();

			T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();
			Integer dalId;

			if (dalClassFields[0].get(dal) == null) {
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT LAST_INSERT_ID()");
				resultSet.next();
				dalId = resultSet.getInt(1);
				statement.close();
			} else {
				dalId = (Integer) dalClassFields[0].get(dal);
			}

			preparedStatement.close();

			ObjectDTO<Integer> objectDTO = new ObjectDTO<>();

			objectDTO.transferData = dalId;
			objectDTO.success = true;
			objectDTO.message = Success.DB_CREATE_SUCCESSFUL.getMessage();

			return objectDTO;
		} catch (Exception e) {
			_log.writeErrorMessage(e, true);
			ObjectDTO<Integer> objectDTO = new ObjectDTO<>();
			objectDTO.message = e.toString() + ".";
			return objectDTO;
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
		try (Connection connection = setDatabaseConnection()) {

			ListDTO<T> listDTO = new ListDTO<>();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();

			String readQuery = createReadQuery(dal, dalClass, dalClassFields);

			Boolean hasCondition = readQuery.contains("WHERE");

			PreparedStatement preparedStatement = connection.prepareStatement(readQuery);
			ResultSet resultSet = preparedStatement.executeQuery();

			List<T> dalList = new ArrayList<>();

			while (resultSet.next()) {

				T returnDAL = (T) Class.forName(dalClass.getName()).getConstructor().newInstance();

				boolean itemDALNonStandartFieldsSetted = false;
				for (int j = 0; j < dalClassFields.length; j++) {

					Class<?> dalField = dalClassFields[j].getType();
					if (dalField != byte[].class && dalField != ItemType.class) {
						dalClassFields[j].set(returnDAL, (dalField.cast(resultSet.getObject(j + 1))));
					} else {
						if (dalClass == ImageDAL.class) {
							ImageDAL imageDAL = (ImageDAL) returnDAL;
							imageDAL.image = resultSet.getBytes("Image");
							returnDAL = (T) imageDAL;
						} else if (dalClass == ItemDAL.class) {
							if (!itemDALNonStandartFieldsSetted) {
								ItemDAL itemDAL = (ItemDAL) returnDAL;
								itemDAL.itemImage = resultSet.getBytes("ItemImage");
								itemDAL.itemType = ItemType.getByItemTypeTitle(resultSet.getString("ItemType"));
								returnDAL = (T) itemDAL;
								itemDALNonStandartFieldsSetted = true;
							}
						}

					}
				}
				dalList.add(returnDAL);
			}
			preparedStatement.closeOnCompletion();

			listDTO.transferDataList = dalList;
			listDTO.success = true;
			listDTO.message = !dalList.isEmpty() ? Success.DB_READ_SUCCESSFUL.getMessage()
					: (hasCondition == true ? Error.DB_CANT_FIND_DATA.getMessage() : Error.DB_EMPTY_TABLE.getMessage());

			return listDTO;
		} catch (Exception e) {
			_log.writeErrorMessage(e, true);
			ListDTO<T> listDTO = new ListDTO<>();
			listDTO.message = e.toString() + ".";
			return listDTO;
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
		try (Connection connection = setDatabaseConnection()) {
			DTO dto = new DTO();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();

			Integer firstFieldValue = (Integer) dalClassFields[0].get(dal);

			if (firstFieldValue == null || firstFieldValue < 1) {
				dto.message = Error.DB_WRON_ID.getMessage();
				_log.writeWarningMessage(Error.DB_UPDATE_FILED.getMessage() + " " + Error.DB_WRON_ID.getMessage(), true,
						"Input object: " + dalClass.getSimpleName() + ".");
				return dto;
			}

			String updateQuery = createUpdateQuery(dal, dalClass, dalClassFields);

			PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
			if (dalClass == ImageDAL.class) {
				ImageDAL imageDAL = (ImageDAL) dal;
				preparedStatement.setBytes(1, imageDAL.image);
			} else if (dalClass == ItemDAL.class) {
				ItemDAL itemDAL = (ItemDAL) dal;
				preparedStatement.setBytes(1, itemDAL.itemImage);
			}
			preparedStatement.executeUpdate();
			preparedStatement.close();

			dto.success = true;
			dto.message = Success.DB_UPDATE_SUCCESSFUL.getMessage();

			return dto;
		} catch (Exception e) {
			_log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = e.toString() + ".";
			return dto;
		}
	}

	/**
	 * Deletes row from the table by an input DAL. Row deletes by the first field
	 * value of a DAL. This field should represent primary or foreign key on a table
	 * and should not be a null or less than 1.
	 */
	@Override
	public <T> DTO delete(T dal) {
		try (Connection connection = setDatabaseConnection()) {
			DTO dto = new DTO();

			Class<?> dalClass = dal.getClass();
			Field[] dalClassFields = dalClass.getFields();

			Integer firstFieldValue = (Integer) dalClassFields[0].get(dal);

			if (firstFieldValue == null || firstFieldValue < 1) {
				dto.message = Error.DB_WRON_ID.getMessage();
				_log.writeWarningMessage(Error.DB_DELETE_FILED.getMessage() + " " + Error.DB_WRON_ID.getMessage(), true,
						"Input object: " + dalClass.getSimpleName() + ".");
				return dto;
			}

			String deleteQuery = createDeleteQuery(dal, dalClass, dalClassFields);

			PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
			preparedStatement.executeUpdate();
			preparedStatement.close();

			dto.success = true;
			dto.message = Success.DB_DELETE_SUCCESSFUL.getMessage();

			return dto;
		} catch (Exception e) {
			_log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = e.toString() + ".";
			return dto;
		}
	}

	private Connection setDatabaseConnection()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		IDatabase database = new DatabaseImpl();
		return database.connect();
	}

	private <T> String createCreateQuery(T dal, Field[] dalClassFields, String tableName)
			throws IllegalAccessException {
		String columnValues = "";

		for (int i = 0; i < dalClassFields.length; i++) {
			if (dalClassFields[i].getType() != byte[].class) {
				columnValues += (dalClassFields[i].getType() == Integer.class || dalClassFields[i].get(dal) == null
						? dalClassFields[i].get(dal) + ", "
						: "\'" + dalClassFields[i].get(dal) + "\', ");
			} else {
				columnValues += "?, ";
			}
		}

		columnValues = columnValues.substring(0, columnValues.length() - 2);
		String createQuery = "INSERT INTO " + tableName + " VALUES (" + columnValues + ");";
		System.out.println("\n" + createQuery);
		return createQuery;
	}

	private <T> String createReadQuery(T dal, Class<?> dalClass, Field[] dalClassFields)
			throws IllegalArgumentException, IllegalAccessException {

		String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";

		if (tableName.equalsIgnoreCase("`Result`")
				&& (dalClassFields[1].get(dal) != null || dalClassFields[2].get(dal) != null
						|| dalClassFields[3].get(dal) != null || dalClassFields[4].get(dal) != null)) {
			return createResultTableReadQuery(dal, dalClassFields);
		}
		if (tableName.equalsIgnoreCase("`Log`") && dalClassFields[2].get(dal) != null
				&& dalClassFields[3].get(dal) != null) {
			return createLogTableReadQuery(dal, dalClassFields);
		}

		String readQuery = "";
		String whereCondition = "";
		Boolean isCondition = false;
		for (int i = 0; i < dalClassFields.length; i++) {

			if (dalClassFields[i].get(dal) != null) {

				Class<?> dalField = dalClassFields[i].getType();

				if (dalField != byte[].class) {
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
		System.out.println("\n" + readQuery);
		return readQuery;
	}

	/**
	 * If FightId is not null, will be created read query for a row with such
	 * FightId. Otherwise the first found userId will be taken and will be created
	 * read query for all result with such userId (no matter win or lose).
	 */
	private <T> String createResultTableReadQuery(T dal, Field[] dalClassFields)
			throws IllegalArgumentException, IllegalAccessException {
		String readQuery = "SELECT * FROM `Result` WHERE ";

		if (dalClassFields[0].get(dal) != null) {
			readQuery += "FightId = " + dalClassFields[0].get(dal) + ";";
		} else {

			for (int i = 1; i < dalClassFields.length; i++) {

				if (dalClassFields[i].get(dal) != null) {
					String userId = dalClassFields[i].get(dal).toString();
					readQuery += "WinUserId = " + userId + " OR LossUserId = " + userId + " OR TieUser1Id = " + userId
							+ " OR TieUser2Id = " + userId + ";";
					break;
				}
			}
		}
		System.out.println("\n" + readQuery);
		return readQuery;
	}

	/**
	 * Will be created read query for a rows at a LogTable where both user's Ids
	 * exists (no matter witch is first and witch is second)
	 */
	private <T> String createLogTableReadQuery(T dal, Field[] dalClassFields)
			throws IllegalArgumentException, IllegalAccessException {
		String user1Id = dalClassFields[2].get(dal).toString();
		String user2Id = dalClassFields[3].get(dal).toString();
		String readQuery = "SELECT * FROM `log` WHERE (User1Id = " + user1Id + " AND User2Id = " + user2Id
				+ ") OR (User1Id = " + user2Id + " AND User2Id = " + user1Id + ");";
		System.out.println("\n" + readQuery);
		return readQuery;
	}

	private <T> String createUpdateQuery(T dal, Class<?> dalClass, Field[] dalClassFields)
			throws IllegalAccessException {
		String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";

		String columnValues = "";

		for (int i = 1; i < dalClassFields.length; i++) {

			if (dalClassFields[i].getType() != byte[].class) {
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
		System.out.println("\n" + updateQuery);
		return updateQuery;
	}

	private <T> String createDeleteQuery(T dal, Class<?> dalClass, Field[] dalClassFields)
			throws IllegalAccessException {
		String tableName = "`" + dalClass.getSimpleName().replace("DAL", "") + "`";
		String columnValue = dalClassFields[0].getName() + " = " + dalClassFields[0].get(dal) + ";";
		String deleteQuery = "DELETE FROM " + tableName + " WHERE " + columnValue;
		System.out.println("\n" + deleteQuery);
		return deleteQuery;
	}

}
