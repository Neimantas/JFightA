import java.sql.Timestamp;

import Models.dal.LogDAL;
import Models.dal.UserDAL;
import Models.dto.DTO;
import Models.dto.ListDTO;
import Models.dto.ObjectDTO;
import services.impl.CRUDImpl;
import services.impl.DatabaseImpl;

public class CrudTest {

	public static void main(String[] args) {

		
		CRUDImpl crud = new CRUDImpl(new DatabaseImpl());
		
		LogDAL logDAL = new LogDAL();
		logDAL.fightId = 1;
		logDAL.log = "Test log";
		logDAL.user2Id = 1;
		
		UserDAL userDAL = new UserDAL();
		userDAL.name = "Kolia";
		userDAL.password = "password";
		
		
		
		ObjectDTO<LogDAL> dtoL = crud.create(logDAL);
		
//		ObjectDTO<UserDAL> dtoU = crud.create(userDAL);
				
//		ListDTO<LogDAL> userDTO = crud.read(logDAL);
//		System.out.println(userDTO.success);
//		System.out.println(userDTO.message);
//		System.out.println(userDTO.transferDataList.get(0).date);
//		System.out.println(userDTO.transferDataList.get(0).fightId);
//		System.out.println(userDTO.transferDataList.get(0).log);
//
//		System.out.println("\n" + userDTO.message);
		
		System.out.println(dtoL.message);
		
		
		
	}

}
