import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

import models.constant.ItemType;
import models.dal.CharacterDAL;
import models.dal.ImageDAL;
import models.dal.ItemDAL;
import models.dal.ResultDAL;
import models.dal.UserDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.impl.CRUDImpl;

public class CrudTest {

	public static void main(String[] args) throws IOException {
		ICRUD crud = CRUDImpl.getInstance();

		// ResultDAL resultDAL = new ResultDAL();
		//// resultDAL.fightId = 1;
		// resultDAL.tieUser2Id = 1;
		//// resultDAL.tieUser1Id = 2;
		//
		// ListDTO<ResultDAL> listDTO = crud.read(resultDAL);
		// System.out.println(listDTO.transferDataList.size());

//		 UserDAL userDAL = new UserDAL();
//		 userDAL.name = "Teeest";
//		 userDAL.password = "fdgfsdgf";
//		 userDAL.eMail = "test@test.lt";
//		 ObjectDTO<UserDAL> listDTOu = crud.create(userDAL);
//		 
//		 CharacterDAL characterDAL = new CharacterDAL();
//		 characterDAL.userId = listDTOu.transferData.userId;
//		 characterDAL.healthPoints = 100;
//		 characterDAL.strenght = 5;
//		 characterDAL.experience = 5;
//		 characterDAL.level = 1;
//		 ObjectDTO<CharacterDAL> listDTOc = crud.create(characterDAL);
		// System.out.println(listDTOu.transferDataList.size());

//		testImage(crud);
		
//		ImageDAL imageDAL = new ImageDAL();
//		imageDAL.imageId = 11;
//		crud.delete(imageDAL);

	}
	
	public static void testItem(ICRUD crud) throws FileNotFoundException, IOException {

		//create
		File file = new File("src\\main\\webapp\\resources\\images\\img-01.jpg");
		
		ItemDAL itemDAL = new ItemDAL();
		itemDAL.itemName = "test";
		itemDAL.itemImage = Files.readAllBytes(file.toPath());
		itemDAL.imageFormat = ".jpg";
		itemDAL.itemType = ItemType.ATTACK;
		itemDAL.description = "test";
		itemDAL.minCharacterLevel = 1;
		itemDAL.attackPoints = 10;
		itemDAL.defencePoints = 5;
		
		ObjectDTO<ItemDAL> uploadDTO = crud.create(itemDAL);

		//update
		ItemDAL createdItemDAL = uploadDTO.transferData;
		createdItemDAL.itemType = ItemType.DEFENCE;
		DTO updateDTO = crud.update(createdItemDAL);

		
	}

	public static void testImage(ICRUD crud) throws FileNotFoundException, IOException {
		// upload image
		File file = new File("src\\main\\webapp\\resources\\images\\characters\\Alex.png");

		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageName = "Alex.png";
		imageDAL.image = Files.readAllBytes(file.toPath());
		imageDAL.userId = 8;

		ObjectDTO<ImageDAL> uploadDTO = crud.create(imageDAL);

		int newImageId = uploadDTO.transferData.imageId;

		// update image
		File updateFile = new File("src\\main\\webapp\\resources\\images\\characters\\default2.jpg");
		ImageDAL updateImageDAL = new ImageDAL();
		updateImageDAL.imageId = newImageId;
		updateImageDAL.imageName = "default2.jpg";
		updateImageDAL.image = Files.readAllBytes(updateFile.toPath());
		updateImageDAL.userId = 2;
		DTO updateDTO = crud.update(updateImageDAL);

		// download image
		ListDTO<ImageDAL> listDTO = crud.read(updateImageDAL);
		String fileName = listDTO.transferDataList.get(0).imageName;
		File newFile = new File("src\\main\\webapp\\resources\\images\\characters\\Downloaded image. " + fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(newFile);

		fileOutputStream.write(listDTO.transferDataList.get(0).image);
		fileOutputStream.close();
		System.out.println(listDTO.message);

		// delete image
		DTO dto = crud.delete(updateImageDAL);
		System.out.println(dto.message);
	}

}
