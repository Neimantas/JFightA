import models.dal.ItemDAL;
import models.dto.ListDTO;
import services.ICRUD;
import services.impl.CRUDImpl;

public class ItemImageTest {
	
	private static ICRUD _crud;
	

	public static void main(String[] args) {
		
		_crud = CRUDImpl.getInstance();
		
		ItemDAL dal = new ItemDAL();

		dal.itemId = 1;

		ListDTO<ItemDAL> dto = _crud.read(dal);
		if(dto.success) {
			
			System.out.println(dto.transferDataList.get(0).itemName);
			
		}
		
	}

}
