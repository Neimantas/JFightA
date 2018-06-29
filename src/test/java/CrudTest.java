import Models.dal.LogDAL;
import Models.dto.DTO;
import services.impl.CRUDImpl;
import services.impl.DatabaseImpl;

public class CrudTest {

	public static void main(String[] args) {

		LogDAL logDAL = new LogDAL();
		CRUDImpl crud = new CRUDImpl(new DatabaseImpl());
		DTO<LogDAL> userDTO = crud.read(logDAL);
		System.out.println(userDTO.success);
		System.out.println(userDTO.message);
		System.out.println(userDTO.transferData.get(0).date);
		System.out.println(userDTO.transferData.get(0).fightId);
		System.out.println(userDTO.transferData.get(0).log);

	}

}
