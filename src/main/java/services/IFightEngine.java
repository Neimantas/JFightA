package services;

import models.business.Actions;
import models.dal.FightDataDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;

public interface IFightEngine {

	ObjectDTO<FightDataDAL> getOpponentData(int fightId, String userId);
	ListDTO<FightDataDAL> engine(int fightId, int roundId, int health, int userId, ActionsDTO actionDTO);

}
