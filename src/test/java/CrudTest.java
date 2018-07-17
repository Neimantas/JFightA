import models.dal.ResultDAL;
import models.dal.UserDAL;
import models.dto.ListDTO;
import services.ICRUD;
import services.impl.CRUDImpl;

public class CrudTest {

	public static void main(String[] args) {
		ICRUD crud = CRUDImpl.getInstance();

//		ResultDAL resultDAL = new ResultDAL();
////		resultDAL.fightId = 1;
//		resultDAL.tieUser2Id = 1;
////		resultDAL.tieUser1Id = 2;
//		
//		ListDTO<ResultDAL> listDTO = crud.read(resultDAL);
//		System.out.println(listDTO.transferDataList.size());
		
		UserDAL userDAL = new UserDAL();
		userDAL.name = "Kolia";
		ListDTO<UserDAL> listDTOu = crud.read(userDAL);
		System.out.println(listDTOu.transferDataList.size());

	}
}
