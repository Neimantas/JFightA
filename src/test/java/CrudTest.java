
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import models.dal.FightDataDAL;
import models.dal.ImageDAL;
import models.dal.ResultDAL;
import models.dal.UserDAL;
import models.dto.DTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.impl.CRUDImpl;
import services.impl.DatabaseImpl;

public class CrudTest {

	public static void main(String[] args) {
		CRUDImpl crud = new CRUDImpl(new DatabaseImpl());

		// ResultDAL resultDAL = new ResultDAL();
		// ObjectDTO<ResultDAL> dto = crud.create(resultDAL);
		// System.out.println(dto.message);
		// System.out.println("FightId: " + dto.transferData.fightId);
		// FightDataDAL fightDataDAL = new FightDataDAL();
		// fightDataDAL.fightId = dto.transferData.fightId;
		// fightDataDAL.userId = 1;
		// ObjectDTO<FightDataDAL> FightDTO = crud.create(fightDataDAL);
		// System.out.println(FightDTO.message);
		// DTO deleteFightDTO = crud.delete(fightDataDAL);
		// System.out.println(deleteFightDTO.message);
		// DTO deleteResultDTO = crud.delete(dto.transferData);
		// System.out.println(deleteFightDTO.message);

		 FightDataDAL fightDataDAL = new FightDataDAL();
		 fightDataDAL.fightId = 1;
		 ListDTO<FightDataDAL> dto = crud.read(fightDataDAL);
		// System.out.println(dto.message);
		// System.out.println(dto.transferDataList.size());

		// FightDataDAL fightDataDAL = new FightDataDAL();
		// fightDataDAL.fightId = 1;
		// fightDataDAL.userId = 1;
		// ObjectDTO<FightDataDAL> dto = crud.create(fightDataDAL);
		// System.out.println(dto.message);

		// UserDAL userDAL = new UserDAL();
		// userDAL.name = "Kolia";
		// userDAL.userId = 1;
		// ListDTO<UserDAL> listDTO = crud.read(userDAL);
		// System.out.println(listDTO.message);
		// System.out.println(listDTO.success);
		// System.out.println(listDTO.transferDataList.size());

//		try {
//			// upload
//			File file = new File("src\\main\\webapp\\resources\\images\\charecters\\Alex.png");
//			FileInputStream fileInputStream = new FileInputStream(file);
//
//			DTO dto = crud.startImageTransferSession();
//			ImageDAL imageDAL = new ImageDAL();
//			imageDAL.imageName = "Alex.png";
//			imageDAL.imageStream = fileInputStream;
//			imageDAL.userId = 1;
//
//			crud.startImageTransferSession();
//			DTO dto2 = crud.uploadImage(imageDAL);
//			fileInputStream.close();
//			crud.endImageTransferSession();
//			System.out.println(dto2.message);
//
//			// download
//			crud.startImageTransferSession();
//			ObjectDTO<ImageDAL> objectDTO = crud.getImage(1);
//			String fileName = objectDTO.transferData.imageName;
//
//			File newFile = new File("src\\main\\webapp\\resources\\images\\charecters\\Downloaded image. " + fileName);
//			FileOutputStream fileOutputStream = new FileOutputStream(newFile);
//			long fileSize = copyStream(objectDTO.transferData.imageStream, fileOutputStream);
//
//			fileOutputStream.close();
//			objectDTO.transferData.imageStream.close();
//			crud.endImageTransferSession();
//
//			System.out.println(objectDTO.message);
//			System.out.println("File size: " + fileSize + " bytes.");
//		} catch (IOException e) {
//			System.out.println(e.getMessage());
//		}
//
//		// Delete image
//		System.out.println(crud.deleteImage(1).message);
//
//	}
//
//	private static long copyStream(InputStream input, OutputStream output) throws IOException {
//		byte[] buffer = new byte[4096];
//		long count = 0L;
//		int n = 0;
//		while (-1 != (n = input.read(buffer))) {
//			output.write(buffer, 0, n);
//			count += n;
//		}
//		return count;
	}

}
