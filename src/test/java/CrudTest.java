

import models.dal.FightDataDAL;
import models.dal.ResultDAL;
import models.dto.DTO;
import models.dto.ObjectDTO;
import services.impl.CRUDImpl;
import services.impl.DatabaseImpl;

public class CrudTest {

	public static void main(String[] args) {
		CRUDImpl crud = new CRUDImpl(new DatabaseImpl());
		
//		ResultDAL resultDAL = new ResultDAL();
//		ObjectDTO<ResultDAL> dto = crud.create(resultDAL);
//		System.out.println(dto.message);
//		System.out.println("FightId: " + dto.transferData.fightId);
//		FightDataDAL fightDataDAL = new FightDataDAL();
//		fightDataDAL.fightId = dto.transferData.fightId;
//		fightDataDAL.userId = 1;
//		ObjectDTO<FightDataDAL> FightDTO = crud.create(fightDataDAL);
//		System.out.println(FightDTO.message);
//		DTO deleteFightDTO = crud.delete(fightDataDAL);
//		System.out.println(deleteFightDTO.message);
//		DTO deleteResultDTO = crud.delete(dto.transferData);
//		System.out.println(deleteFightDTO.message);
		
//		DTO uploadImageDTO = crud.uploadImage(1, "Alex.png");
//		System.out.println(uploadImageDTO.message);
		
//		DTO downloadImageDTO = crud.getImage(1);
//		System.out.println(downloadImageDTO.message);
		
//		DTO deleteDTO = crud.deleteImage(1);
//		System.out.println(deleteDTO.message);
		
	}

}
