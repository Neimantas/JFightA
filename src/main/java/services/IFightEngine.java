package services;

import models.dal.FightDataDAL;
import models.dto.ObjectDTO;

public interface IFightEngine {
	
	ObjectDTO<FightDataDAL> getOpponentName(int fightId, String userId);

}
