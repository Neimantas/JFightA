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
	public ObjectDTO<FightDataDAL> logFightData(int fightId, int userIdA, int userIdB) {
		JSONArray json = new JSONArray();

		FightDataDAL dalF = new FightDataDAL();

		dalF.fightId = fightId;
		ListDTO<FightDataDAL> dtoF = crud.<FightDataDAL>read(dalF);
		if (dtoF.success) {
			List<FightDataDAL> list = dtoF.transferDataList;
			for (FightDataDAL d : list) {
				JSONObject jsonOb = new JSONObject();

				jsonOb.put("u", d.userId);
				jsonOb.put("r", d.round);
				jsonOb.put("hp", d.healthPoints);
				if (d.damage == null) {
					jsonOb.put("d", 0);
				} else {
					jsonOb.put("d", d.damage);
				}
				jsonOb.put("ah", d.attackHead);
				jsonOb.put("ab", d.attackBody);
				jsonOb.put("ahn", d.attackHands);
				jsonOb.put("al", d.attackLegs);
				jsonOb.put("dh", d.defenceHead);
				jsonOb.put("db", d.defenceBody);
				jsonOb.put("dhn", d.defenceHands);
				jsonOb.put("dl", d.defenceLegs);

				json.put(jsonOb);
			}

			System.out.println(json.toString());

			LogDAL dalL = new LogDAL();

			dalL.fightId = fightId;
			dalL.user1Id = userIdA;
			dalL.user2Id = userIdB;
			dalL.log = json.toString();

			ObjectDTO<LogDAL> dtoL = crud.<LogDAL>create(dalL);

			ObjectDTO<FightDataDAL> retSuccess = new ObjectDTO();
			retSuccess.success = true;
			retSuccess.message = "Good message";

			return retSuccess;
		}

		ObjectDTO<FightDataDAL> retFailure = new ObjectDTO();
		retFailure.success = false;
		retFailure.message = "Error message";

		return retFailure;
	}

	@Override
	public ListDTO<FightDataDAL> getLogs(int userIdA, int userIdB) {

		LogDAL dalL = new LogDAL();

		dalL.user1Id = userIdA;
		dalL.user2Id = userIdB;

		ListDTO<LogDAL> dtoL = crud.<LogDAL>read(dalL);
		if (dtoL.success) {
			List<FightDataDAL> returnList = new ArrayList<>();
			List<LogDAL> list = dtoL.transferDataList;
			for (LogDAL l : list) {
				JSONArray json = new JSONArray(l.log);

				for (int i = 0; i < json.length(); i++) {
					JSONObject jsonObj = (JSONObject) json.get(i);
					FightDataDAL returnDAL = new FightDataDAL();

					returnDAL.round = (Integer) jsonObj.get("r");
					returnDAL.userId = (Integer) jsonObj.get("u");
					returnDAL.healthPoints = (Integer) jsonObj.get("hp");
					returnDAL.damage = (Integer) jsonObj.get("d");
					returnDAL.attackHead = (Integer) jsonObj.get("ah");
					returnDAL.attackBody = (Integer) jsonObj.get("ab");
					returnDAL.attackHands = (Integer) jsonObj.get("ahn");
					returnDAL.attackLegs = (Integer) jsonObj.get("al");
					returnDAL.defenceHead = (Integer) jsonObj.get("dh");
					returnDAL.defenceBody = (Integer) jsonObj.get("db");
					returnDAL.defenceHands = (Integer) jsonObj.get("dhn");
					returnDAL.defenceLegs = (Integer) jsonObj.get("dl");

					returnList.add(returnDAL);
				}
			}

			ListDTO<FightDataDAL> retSuccess = new ListDTO();
			retSuccess.success = true;
			retSuccess.message = "Correct given users.";
			retSuccess.transferDataList = returnList;

			return retSuccess;
		}

		ListDTO<FightDataDAL> retFailure = new ListDTO();
		retFailure.success = false;
		retFailure.message = "Error! No such users.";

		System.out.println("Failure");

		return retFailure;
	}

}
