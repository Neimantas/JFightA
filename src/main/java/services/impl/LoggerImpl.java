package services.impl;

import java.util.List;

import models.dal.FightDataDAL;
import models.dal.LogDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.ILogger;

public class LoggerImpl implements ILogger {
	
	private ICRUD crud;
	
	public LoggerImpl() {
		crud = new CRUDImpl(new DatabaseImpl());
	}

	@Override
	public ObjectDTO<FightDataDAL> logFightData(int fightId) {

		FightDataDAL dal = new FightDataDAL();
		
		dal.fightId = fightId;
//		ListDTO<FightDataDAL> ldto = crud.read(dal);
//		if(ldto.success) {
//			List<FightDataDAL> list = ldto.transferDataList;
//			 while(list.next()) {
//				 FightDataDAL fightTag = new  FightDataDAL();
//				 fightTag.fightId = list;
//				 fightTag.tag = resultSet.getInt("Tag");
//				 fightTag.title = resultSet.getString("Title");
//				 fightTag.helloWorldTopicId = resultSet.getByte("HelloWorldTopicid");
//				
//				 languageList.add(fightTag);
//				 }
//		}
		
		return null;
	}

	@Override
	public ObjectDTO<LogDAL> showLogs(int userIdA, int userIdB) {

		
		
		return null;
	}
	
}
