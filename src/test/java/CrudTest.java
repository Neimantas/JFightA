
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
import services.ICRUD;
import services.impl.CRUDImpl;
import services.impl.DatabaseImpl;

public class CrudTest {

	public static void main(String[] args) {
		ICRUD crud = CRUDImpl.getInstance();

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

		 System.out.println(dto.message);
		 System.out.println(dto.transferDataList.size());

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

	}
}
