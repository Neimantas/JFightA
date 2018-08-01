package services.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import models.dal.FightDataDAL;
import models.dal.LogDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.IHigherService;
import services.ILogger;

public class LoggerImpl implements ILogger {

	private IHigherService _hService;

	public LoggerImpl(CRUDImpl crud, HigherService hService) {
		_hService = hService;
	}

	@Override
	public ObjectDTO<FightDataDAL> logFightData(int fightId, int userIdA, int userIdB) {
		JSONArray json = new JSONArray();

		ListDTO<FightDataDAL> dtoF = _hService.getFightDataDAL(fightId);
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
				jsonOb.put("ahn", d.attackArms);
				jsonOb.put("al", d.attackLegs);
				jsonOb.put("dh", d.defenceHead);
				jsonOb.put("db", d.defenceBody);
				jsonOb.put("dhn", d.defenceArms);
				jsonOb.put("dl", d.defenceLegs);

				json.put(jsonOb);
			}

			ObjectDTO<Integer> dtoL = _hService.logFightDataDAL(fightId, userIdA, userIdB, json.toString());
			if (dtoL.success) {
				ObjectDTO<FightDataDAL> retSuccess = new ObjectDTO<FightDataDAL>();
				retSuccess.success = true;
				retSuccess.message = dtoL.message;

				return retSuccess;
			} else {
				ObjectDTO<FightDataDAL> retFailure = new ObjectDTO<FightDataDAL>();
				retFailure.success = false;
				retFailure.message = dtoL.message;

				return retFailure;
			}
		}

		ObjectDTO<FightDataDAL> retFailure = new ObjectDTO<FightDataDAL>();
		retFailure.success = false;
		retFailure.message = dtoF.message;

		return retFailure;
	}

	@Override
	public ListDTO<String> getLogs(int userIdA, int userIdB) {

		ListDTO<LogDAL> dtoLog = _hService.logInfoDAL(userIdA, userIdB);
		if (dtoLog.success) {

			List<String> returnList = makeTableFromFightData(dtoLog);

			ListDTO<String> retSuccess = new ListDTO<String>();
			retSuccess.success = true;
			retSuccess.message = dtoLog.message;
			retSuccess.transferDataList = returnList;

			return retSuccess;
		}

		ListDTO<String> retFailure = new ListDTO<String>();
		retFailure.success = false;
		retFailure.message = dtoLog.message;

		System.out.println("Failure");

		return retFailure;
	}

	private List<String> makeTableFromFightData(ListDTO<LogDAL> dtoLog) {
		List<String> returnList = new ArrayList<>();
		List<LogDAL> list = dtoLog.transferDataList;
		for (LogDAL l : list) {
			JSONArray json = new JSONArray(l.log);

			String rows = makeAndFillTableRow(json);

			String table = "						<div class=\"container\">\r\n"
					+ "									<h6>Fight ID: " + l.fightId + ", date of match: " + l.date
					+ "</h6>\r\n"

					+ "										<table class=\"table-sm table-bordered\">\r\n"
					+ "											<thead class=\"thead-dark\">\r\n"
					+ "												<tr>\r\n"
					+ "													<th scope=\"col\">Round</th>\r\n"
					+ "													<th scope=\"col\">UserID</th>\r\n"
					+ "													<th scope=\"col\">Health Points</th>\r\n"
					+ "													<th scope=\"col\">Damage</th>\r\n"
					+ "													<th scope=\"col\">Attack Head</th>\r\n"
					+ "													<th scope=\"col\">Attack Body</th>\r\n"
					+ "													<th scope=\"col\">Attack Hands</th>\r\n"
					+ "													<th scope=\"col\">Attack Legs</th>\r\n"
					+ "													<th scope=\"col\">Defence Head</th>\r\n"
					+ "													<th scope=\"col\">Defence Body</th>\r\n"
					+ "													<th scope=\"col\">Defence Hands</th>\r\n"
					+ "													<th scope=\"col\">Defence Legs</th>\r\n"
					+ "												</tr>\r\n"
					+ "											</thead>\r\n"
					+ "											<tbody>\r\n" + rows
					+ "											</tbody>\r\n"
					+ "										</table>\r\n" + "								</div>"
					+ "                            <br>";

			returnList.add(table);
		}
		return returnList;
	}

	private String makeAndFillTableRow(JSONArray json) {
		String rows = "";
		for (int i = 0; i < json.length(); i++) {
			JSONObject jsonObj = (JSONObject) json.get(i);

			rows += "													<tr>\r\n"
					+ "														<th scope=\"row\">" + jsonObj.get("r")
					+ "</th>\r\n" + "														<td>" + jsonObj.get("u")
					+ "</td>\r\n" + "														<td>" + jsonObj.get("hp")
					+ "</td>\r\n" + "														<td>" + jsonObj.get("d")
					+ "</td>\r\n" + "														<td>"
					+ changeATKChar((Integer) jsonObj.get("ah")) + "</td>\r\n"
					+ "														<td>"
					+ changeATKChar((Integer) jsonObj.get("ab")) + "</td>\r\n"
					+ "														<td>"
					+ changeATKChar((Integer) jsonObj.get("ahn")) + "</td>\r\n"
					+ "														<td>"
					+ changeATKChar((Integer) jsonObj.get("al")) + "</td>\r\n"
					+ "														<td>"
					+ changeDEFChar((Integer) jsonObj.get("dh")) + "</td>\r\n"
					+ "														<td>"
					+ changeDEFChar((Integer) jsonObj.get("db")) + "</td>\r\n"
					+ "														<td>"
					+ changeDEFChar((Integer) jsonObj.get("dhn")) + "</td>\r\n"
					+ "														<td>"
					+ changeDEFChar((Integer) jsonObj.get("dl")) + "</td>\r\n"
					+ "													</tr>";
		}
		return rows;
	}

	private String changeATKChar(int a) {
		String string = null;

		if (a == 1) {
			string = "ATK";
		} else {
			string = "-";
		}

		return string;
	}

	private String changeDEFChar(int a) {
		String string = null;

		if (a == 1) {
			string = "DEF";
		} else {
			string = "-";
		}

		return string;
	}

}
