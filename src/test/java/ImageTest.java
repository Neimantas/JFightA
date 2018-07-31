import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import models.constant.ImageType;
import services.IImage;
import services.impl.CRUDImpl;
import services.impl.ImageImpl;

public class ImageTest {

	public static void main(String[] args) {

		// testDeleteImage();
		 try {
		  testAddImage();
//		 testEditImage();
		 } catch (IOException e) {
		 }

	}

	private static void testAddImage() throws IOException {

		IImage item = new ImageImpl(new CRUDImpl());

		File file = new File("src\\main\\webapp\\resources\\images\\characters\\alex.png");
		byte[] image = Files.readAllBytes(file.toPath());
		String imageName = "Alex";
		item.addImage(11, imageName, ImageType.PNG, image);

	}

	private static void testEditImage() throws IOException {

		IImage item = new ImageImpl(new CRUDImpl());

		File file = new File("src\\main\\webapp\\resources\\images\\characters\\chun-li.jpg");
		byte[] image = Files.readAllBytes(file.toPath());
		String imageName = "chun-li";
		item.editImage(11, imageName, ImageType.JPG, image);
	}

	private static void testDeleteImage() {

		IImage item = new ImageImpl(new CRUDImpl());
		item.deleteImage(10);

	}
}
