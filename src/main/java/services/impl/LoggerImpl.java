package services.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
		JSONArray json = new JSONArray();

		FightDataDAL dalF = new FightDataDAL();
		
		dalF.fightId = fightId;
		ListDTO<FightDataDAL> dtoF = crud.<FightDataDAL>read(dalF);
		if(dtoF.success) {
			List<FightDataDAL> list =dtoF.transferDataList;
			for(FightDataDAL  d: list) {
				JSONObject jsonOb = new JSONObject();
				
				jsonOb.put("UserId", d.userId);
				jsonOb.put("Round", d.round);
				jsonOb.put("HealthPoints", d.healthPoints);
				jsonOb.put("Damage", d.damage);
				jsonOb.put("AttackHead", d.attackHead);
				jsonOb.put("AttackBody", d.attackBody);
				jsonOb.put("AttackHands", d.attackHands);
				jsonOb.put("AttackLegs", d.attackLegs);
				jsonOb.put("DefenceHead", d.defenceHead);
				jsonOb.put("DefenceBody", d.defenceBody);
				jsonOb.put("DefenceHands", d.defenceHands);
				jsonOb.put("DefenceLegs", d.defenceLegs);
				
				json.put(jsonOb);
			}
			
			System.out.println(json.toString());
			
			LogDAL dalL = new LogDAL();

			dalL.fightId = fightId;
			dalL.user1Id = 1;//reikalingas normalus ID
			dalL.user2Id = 2;//reikalingas normalus ID
			dalL.log = json.toString();
			
			ObjectDTO<LogDAL> dtoL = crud.<LogDAL>create(dalL);
		}
		
		return null;
	}

	@Override
	public ObjectDTO<LogDAL> showLogs(int userIdA, int userIdB) {
		
		LogDAL dalL = new LogDAL();
		
		dalL.user1Id = 1;//reikalingas normalus ID
		dalL.user2Id = 2;//reikalingas normalus ID
		
		ListDTO<LogDAL> dtoL = crud.<LogDAL>read(dalL);
		if(dtoL.success) {
			List<LogDAL> list = dtoL.transferDataList;
			for(LogDAL l : list) {
				JSONArray json = new JSONArray(l.log);
				for (int i = 0; i < json.length(); i++) {
					JSONObject jsonObj = json.getJSONObject(i);

		            System.out.println(jsonObj);
				}
			}
		}
		
		return null;
	}
	
}
