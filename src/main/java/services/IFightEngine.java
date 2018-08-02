package services;

import models.business.Actions;
import models.dal.FightDataDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;

public interface IFightEngine {

	ObjectDTO<FightDataDAL> getOpponentData(int fightId, int round, int userId);
	ListDTO<FightDataDAL> engine(int fightId, int roundId, int health, int userId, Actions actionDTO);

}
