import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import models.dal.ImageDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;
import services.IImage;
import services.impl.ImageImpl;

public class ImageTest {

	public static void main(String[] args) {
		IImage image = ImageImpl.getInstance();

		try {
			// upload
			File file = new File("src\\main\\webapp\\resources\\images\\charecters\\Alex.png");
			FileInputStream fileInputStream = new FileInputStream(file);

			DTO dto = image.startImageTransferSession();
			ImageDAL imageDAL = new ImageDAL();
			imageDAL.imageName = "Alex.png";
			imageDAL.imageStream = fileInputStream;
			imageDAL.userId = 1;

			image.startImageTransferSession();
			DTO dto2 = image.uploadImage(imageDAL);
			fileInputStream.close();
			image.endImageTransferSession();
			System.out.println(dto2.message);

			// download
			image.startImageTransferSession();
			ObjectDTO<ImageDAL> objectDTO = image.getImage(1);
			String fileName = objectDTO.transferData.imageName;

			File newFile = new File("src\\main\\webapp\\resources\\images\\charecters\\Downloaded image. " + fileName);
			FileOutputStream fileOutputStream = new FileOutputStream(newFile);
			long fileSize = copyStream(objectDTO.transferData.imageStream, fileOutputStream);

			fileOutputStream.close();
			objectDTO.transferData.imageStream.close();
			image.endImageTransferSession();

			System.out.println(objectDTO.message);
			System.out.println("File size: " + fileSize + " bytes.");
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

		// Delete image
		System.out.println(image.deleteImage(1).message);

	}

	private static long copyStream(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte[4096];
		long count = 0L;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}

}
