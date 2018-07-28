import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import models.business.CharacterInfo;
import models.business.Player;
import models.business.User;
import models.constant.ItemType;
import models.constant.UserStatus;
import models.dal.CharacterDAL;
import models.dal.ItemDAL;
import models.dal.UserDAL;
import models.dto.ListDTO;
import services.ICRUD;
import services.ICache;
import services.IImage;
import services.impl.CRUDImpl;
import services.impl.CacheImpl;
import services.impl.ImageImpl;

public class ImageTest {

	public static void main(String[] args) {

		// testDeleteImage();
		// try {
		// // testAddImage();
		// testEditImage();
		// } catch (IOException e) {
		// }

	}

	private static void testAddImage() throws IOException {

		IImage item = new ImageImpl(new CRUDImpl());

		File file = new File("src\\main\\webapp\\resources\\images\\characters\\Alex.png");
		byte[] image = Files.readAllBytes(file.toPath());
		String imageName = "Alex.png";
		item.addImage(11, imageName, image);

	}

	private static void testEditImage() throws IOException {

		IImage item = new ImageImpl(new CRUDImpl());

		File file = new File("src\\main\\webapp\\resources\\images\\characters\\chun-li.jpg");
		byte[] image = Files.readAllBytes(file.toPath());
		String imageName = "chun-li.jpg";
		item.editImage(11, imageName, image);
	}

	private static void testDeleteImage() {

		IImage item = new ImageImpl(new CRUDImpl());
		item.deleteImage(10);

	}
}
