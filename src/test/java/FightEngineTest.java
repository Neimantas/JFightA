import models.dal.FightDataDAL;
import models.dto.ActionsDTO;
import models.dto.ObjectDTO;
import services.impl.FightEngineImpl;

public class FightEngineTest {

	public static void main(String[] args) {

		FightEngineImpl fe = new FightEngineImpl();
		ObjectDTO<FightDataDAL> obj = fe.getOpponentData(1, 0, "1");
		
		ActionsDTO actions = new ActionsDTO();
		actions.attackArms = 1;
		actions.attackBody = 1;
		actions.attackHead = 1;
		actions.attackLegs = 1;
		fe.engine(1, 2, 100, "2", actions);
		
	}

}
