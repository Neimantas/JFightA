import models.dal.FightDataDAL;
import models.dto.ActionsDTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.impl.CRUDImpl;
import services.impl.DatabaseImpl;
import services.impl.FightEngineImpl;

public class FightEngineTest {

	public static void main(String[] args) {

//		FightEngineImpl fe = new FightEngineImpl();
//		ObjectDTO<FightDataDAL> obj = fe.getOpponentData(1, 0, "1");
//		
//		ActionsDTO actions = new ActionsDTO();
//		actions.attackArms = 1;
//		actions.attackBody = 1;
//		actions.attackHead = 1;
//		actions.attackLegs = 1;
//		fe.engine(1, 2, 100, "2", actions);
		
		ICRUD crud = CRUDImpl.getInstance();
		FightDataDAL dal = new FightDataDAL();
		
		ListDTO<FightDataDAL> ret = crud.<FightDataDAL>read(dal);
		if(ret.success) {
			for(FightDataDAL f : ret.transferDataList) {
				System.out.println("FightId " + f.fightId + " round " + f.round);
			}
		}
	}

}
