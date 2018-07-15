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

	private IDatabase database;
	private Connection connection;
	private PreparedStatement preparedStatement;
	private ILog log;
	private static IImage image;

	private ImageImpl() {
		database = DatabaseImpl.getInstance();
		log = LogImpl.getInstance();
		image = this;
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
			preparedStatement = connection
					.prepareStatement("INSERT INTO `image` (UserId, Image, ImageName) VALUES (?, ?, ?)");
			preparedStatement.setInt(1, imageDAL.userId);
			preparedStatement.setBinaryStream(2, imageDAL.inputStream);
			preparedStatement.setString(3, imageDAL.imageName);

			preparedStatement.executeUpdate();

			DTO dto = new DTO();
			dto.success = true;
			dto.message = "Image uploaded to database successfully.";

			return dto;
		} catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.writeErrorMessage(e, true);
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
			preparedStatement = connection.prepareStatement("SELECT * FROM `image` WHERE ImageId = " + imageId + ";");
			ResultSet resultSet = preparedStatement.executeQuery();

			ObjectDTO<ImageDAL> objectDTO = new ObjectDTO<>();
			ImageDAL imageDAL = new ImageDAL();

			if (resultSet.next()) {

				imageDAL.imageId = imageId;
				imageDAL.userId =  (Integer)resultSet.getObject("UserId");
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
			log.writeErrorMessage(e, true);
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
			preparedStatement = connection
					.prepareStatement("SELECT UserId FROM `image` WHERE UserId = " + userId + ";");
			ResultSet resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				preparedStatement.close();
				preparedStatement = connection.prepareStatement("DELETE FROM `image` WHERE UserId = " + userId + ";");
				preparedStatement.executeUpdate();
			} else {
				dto.message = "There are now image in a database with such Id.";
				return dto;
			}

			dto.success = true;
			dto.message = "Image deleted from the database successfully.";

			return dto;
		} catch (SQLException e) {
			log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = "Database error. " + e.getMessage() + ".";
			return dto;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			log.writeErrorMessage(e, true);
			DTO dto = new DTO();
			dto.message = e.getMessage() + ".";
			return dto;
		} finally {
			closeConnection();
		}
	}

	private void setConnection()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (connection == null || connection.isClosed()) {
			connection = database.connect();
		}
	}

	private String closeConnection() {
		try {
			if (preparedStatement != null && !preparedStatement.isClosed()) {
				preparedStatement.close();
			}
			database.closeConnection();
			return null;
		} catch (SQLException e) {
			log.writeErrorMessage(e, true);
			return e.getMessage();
		}
	}

	public static IImage getInstance() {
		if (image == null) {
			image = new ImageImpl();
		}
		return image;
	}

}
