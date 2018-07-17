package services.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.dal.ImageDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;
import services.IDatabase;
import services.IImage;
import services.ILog;

public class ImageImpl implements IImage {
	private static IImage _image = new ImageImpl();

	private IDatabase _database;
	private Connection _connection;
	private PreparedStatement _preparedStatement;
	private ILog _log;

	private ImageImpl() {
		_database = DatabaseImpl.getInstance();
		_log = LogImpl.getInstance();
	}

	/**
	 * Uploads an image to the database. startImageTransferSession(); should be
	 * executed before calling this method. endImageTransferSession(); should be
	 * executed after image transfer.
	 */
	@Override
	public DTO uploadImage(ImageDAL imageDAL) {
		try {
			setConnection();
			_preparedStatement = _connection
					.prepareStatement("INSERT INTO `image` (UserId, Image, ImageName) VALUES (?, ?, ?)");
			_preparedStatement.setInt(1, imageDAL.userId);
			_preparedStatement.setBinaryStream(2, imageDAL.inputStream);
			_preparedStatement.setString(3, imageDAL.imageName);

			_preparedStatement.executeUpdate();

			DTO dto = new DTO();
			dto.success = true;
			dto.message = "Image uploaded to database successfully.";

			return dto;
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			_log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = e.toString() + ".";
			return dto;
		} finally {
			closeConnection();
		}
	}

	/**
	 * Downloads image from the database. startImageTransferSession(); should be
	 * executed before calling this method. endImageTransferSession(); should be
	 * executed after image transfer.
	 */
	@Override
	public ObjectDTO<ImageDAL> getImage(int imageId) {
		try {
			setConnection();
			_preparedStatement = _connection.prepareStatement("SELECT * FROM `image` WHERE ImageId = " + imageId + ";");
			ResultSet resultSet = _preparedStatement.executeQuery();

			ObjectDTO<ImageDAL> objectDTO = new ObjectDTO<>();
			ImageDAL imageDAL = new ImageDAL();

			if (resultSet.next()) {

				imageDAL.imageId = imageId;
				imageDAL.userId = (Integer) resultSet.getObject("UserId");
				imageDAL.inputStream = resultSet.getBinaryStream("Image");
				imageDAL.imageName = resultSet.getString("ImageName");

			} else {
				objectDTO.message = "There are now image in a database with such Id.";
				objectDTO.success = true;
				return objectDTO;
			}

			objectDTO.transferData = imageDAL;
			objectDTO.success = true;
			objectDTO.message = "Image downloaded from the database successfully.";

			return objectDTO;
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			_log.writeErrorMessage(e, true);
			ObjectDTO<ImageDAL> objectDTO = new ObjectDTO<>();
			objectDTO.message = e.toString() + ".";
			return objectDTO;
		} finally {
			closeConnection();
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
			_preparedStatement = _connection
					.prepareStatement("SELECT UserId FROM `image` WHERE UserId = " + userId + ";");
			ResultSet resultSet = _preparedStatement.executeQuery();

			if (resultSet.next()) {
				_preparedStatement.close();
				_preparedStatement = _connection.prepareStatement("DELETE FROM `image` WHERE UserId = " + userId + ";");
				_preparedStatement.executeUpdate();
			} else {
				dto.message = "There are now image in a database with such Id.";
				return dto;
			}

			dto.success = true;
			dto.message = "Image deleted from the database successfully.";

			return dto;
		} catch (SQLException e) {
			_log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			_log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = e.getMessage() + ".";
			return dto;
		} finally {
			closeConnection();
		}
	}

	private void setConnection()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (_connection == null || _connection.isClosed()) {
			_connection = _database.connect();
		}
	}

	private String closeConnection() {
		try {
			if (_preparedStatement != null && !_preparedStatement.isClosed()) {
				_preparedStatement.close();
			}
			_database.closeConnection();
			return null;
		} catch (SQLException e) {
			_log.writeErrorMessage(e, true);
			return e.getMessage();
		}
	}

	public static IImage getInstance() {
		return _image;
	}

}
