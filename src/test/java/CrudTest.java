import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import models.dal.ImageDAL;
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

		// UserDAL userDAL = new UserDAL();
		// userDAL.name = "Kolia";
		// ListDTO<UserDAL> listDTOu = crud.read(userDAL);
		// System.out.println(listDTOu.transferDataList.size());

		// upload image
		File file = new File("src\\main\\webapp\\resources\\images\\characters\\Alex.png");
		FileInputStream fileInputStream = new FileInputStream(file);

		ImageDAL imageDAL = new ImageDAL();
		imageDAL.imageName = "Alex.png";
		imageDAL.image = fileInputStream;
		imageDAL.userId = 1;

		ObjectDTO<ImageDAL> dto2 = crud.create(imageDAL);
		fileInputStream.close();

		int newImageId = dto2.transferData.imageId;

		// download image
		ImageDAL readImageDAL = new ImageDAL();
		readImageDAL.imageId = newImageId;
		ListDTO<ImageDAL> listDTO = crud.read(readImageDAL);
		String fileName = listDTO.transferDataList.get(0).imageName;
		File newFile = new File("src\\main\\webapp\\resources\\images\\characters\\Downloaded image. " + fileName);
		FileOutputStream fileOutputStream = new FileOutputStream(newFile);
		long fileSize = copyStream(listDTO.transferDataList.get(0).image, fileOutputStream);
		fileOutputStream.close();

		System.out.println(listDTO.message);
		System.out.println("File size: " + fileSize + " bytes.");

		// delete image
		DTO dto = crud.delete(readImageDAL);
		System.out.println(dto.message);

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
