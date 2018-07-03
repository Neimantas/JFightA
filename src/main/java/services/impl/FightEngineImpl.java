package services.impl;

import java.util.List;

import models.dal.FightDataDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IFightEngine;

public class FightEngineImpl implements IFightEngine {
	
	private ICRUD crud;

	public FightEngineImpl() {
		crud = new CRUDImpl(new DatabaseImpl());
	}
	
	@Override
	public ObjectDTO<FightDataDAL> getOpponentName(int fightId, String userName) {
		
		//temporary solution, only for testing purposes
		int userId = Integer.parseInt(userName);
		//
		FightDataDAL dal = new FightDataDAL();
		dal.fightId = fightId;
		ListDTO<FightDataDAL> ldto = crud.<FightDataDAL>read(dal);
		if(ldto.success) {
			int counter = 0;
			while(ldto.transferDataList.size() < 2 && counter < 10000) {
				ldto = crud.<FightDataDAL>read(dal);
				counter++;
			}
			if(ldto.transferDataList.size() == 2) {
				List<FightDataDAL> data = ldto.transferDataList;
				FightDataDAL retDAL = null;
				for(FightDataDAL d : data) {
					if(d.userId != userId) {
						retDAL = d;
						break;
					}
				}
				if (retDAL == null) {
					ObjectDTO<FightDataDAL> retFailure = new ObjectDTO<>();
					retFailure.success = false;
					retFailure.message = "Cannot find opponent";
					return retFailure;
				}
				
				ObjectDTO<FightDataDAL> retSuccess = new ObjectDTO<FightDataDAL>();
				retSuccess.success = true;
				retSuccess.message = "message";
				retSuccess.transferData = retDAL;
				return retSuccess;
			}
			
		}
		ObjectDTO<FightDataDAL> retFailure = new ObjectDTO<>();
		retFailure.success = false;
		retFailure.message = ldto.message;
		return retFailure;
		
	}

}
