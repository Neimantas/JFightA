package services;

import models.dal.FightDataDAL;
import models.dal.LogDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;

public interface ILogger {

	ObjectDTO<FightDataDAL> logFightData(int fightId, int userIdA, int userIdB);
	ListDTO<FightDataDAL> getLogs(int userIdA, int userIdB);

}
