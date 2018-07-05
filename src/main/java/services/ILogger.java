package services;

import models.dal.FightDataDAL;
import models.dal.LogDAL;
import models.dto.ObjectDTO;

public interface ILogger {
	
	ObjectDTO<FightDataDAL> logFightData(int fightId);
	ObjectDTO<LogDAL> showLogs(int userIdA, int userIdB);

}
