package services.impl;

import java.util.ArrayList;
import java.util.List;

import models.constant.CharacterBodyPart;
import models.dal.FightDataDAL;
import models.dto.ActionsDTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IFightEngine;

public class FightEngineImpl implements IFightEngine {
	
	private ICRUD crud;

	public FightEngineImpl() {
		crud = CRUDImpl.getInstance();
	}
	
	@Override
	public ObjectDTO<FightDataDAL> getOpponentData(int fightId, String userID) {
		
		//temporary solution, only for testing purposes
		int userId = Integer.parseInt(userID);
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
	
	
	
	public ObjectDTO<FightDataDAL> getOpponentData(int fightId, int round, String userID) {
		
		//temporary solution, only for testing purposes
		int userId = Integer.parseInt(userID);
		//
		FightDataDAL dal = new FightDataDAL();
		dal.fightId = fightId;
		ListDTO<FightDataDAL> ldto = crud.<FightDataDAL>read(dal);
		if(ldto.success) {
//			int counter = 0;
//			while(ldto.transferDataList.size() < 2 && counter < 10000) {
//				ldto = crud.<FightDataDAL>read(dal);
//				counter++;
//			}
			
			List<FightDataDAL> data = ldto.transferDataList;
			FightDataDAL retDAL = null;
			for(FightDataDAL d : data) {
				if(d.userId != userId && d.round == round) {
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
			
		
		ObjectDTO<FightDataDAL> retFailure = new ObjectDTO<>();
		retFailure.success = false;
		retFailure.message = ldto.message;
		return retFailure;
		
	}

	@Override
	public ListDTO<FightDataDAL> engine(int fightId, int roundId, int health,  String userId, ActionsDTO yourAction) {
		
		FightDataDAL insertDAL = new FightDataDAL();
		insertDAL.fightId = fightId;
		insertDAL.round = roundId;
		insertDAL.userId = Integer.parseInt(userId); //temporary solution
		insertDAL.healthPoints = health;
		insertDAL.attackHead = yourAction.attackHead;
		insertDAL.attackBody = yourAction.attackBody;
		insertDAL.attackHands = yourAction.attackArms;
		insertDAL.attackLegs = yourAction.attackLegs;
		insertDAL.defenceHead = yourAction.defenceHead;
		insertDAL.defenceBody = yourAction.defenceBody;
		insertDAL.defenceHands = yourAction.defenceArms;
		insertDAL.defenceLegs = yourAction.defenceLegs;
		
		crud.<FightDataDAL>create(insertDAL);
		
		int counter = 0;
		ObjectDTO<FightDataDAL> obj = getOpponentData(fightId, roundId, userId);
		while(counter < 1000) {
			if(!obj.success && obj.message.equals("Cannot find opponent")) {
				counter++;
				obj = getOpponentData(fightId, roundId, userId);
				System.out.println("In waiting loop...");
			} else {
				break;
			}
		}
		 
		if(!obj.success && obj.message.equals("Cannot find opponent"))	{
			System.out.println("ERROR: " + obj.message);
			System.out.println("Here current user action data will be written");
			System.out.println("");
			ListDTO<FightDataDAL> ret = new ListDTO<FightDataDAL>();
			ret.success = false;
			ret.message = obj.message;
			return ret;
		}
			
		
		
		
		FightDataDAL opponentDAL = obj.transferData;
		ActionsDTO opponentActions = new ActionsDTO();
		
		opponentActions.attackHead = opponentDAL.attackHead == null?0:opponentDAL.attackHead;
		opponentActions.attackBody = opponentDAL.attackBody == null?0:opponentDAL.attackBody;
		opponentActions.attackArms = opponentDAL.attackHands == null?0:opponentDAL.attackHands;
		opponentActions.attackLegs = opponentDAL.attackLegs == null?0:opponentDAL.attackLegs;
		
		opponentActions.defenceHead = opponentDAL.defenceHead == null?0:opponentDAL.defenceHead;
		opponentActions.defenceBody = opponentDAL.defenceBody == null?0:opponentDAL.defenceBody;
		opponentActions.defenceArms = opponentDAL.defenceHands == null?0:opponentDAL.defenceHands;
		opponentActions.defenceLegs = opponentDAL.defenceLegs == null?0:opponentDAL.defenceLegs;
		
		int[] damages = calculateDamage(yourAction, opponentActions);
		
		int yourHealth = health + damages[1];
		int opponentHealth = opponentDAL.healthPoints + damages[0];
		
		FightDataDAL yourDAL = new FightDataDAL();
		yourDAL.fightId = fightId;
		yourDAL.round = roundId;
		yourDAL.userId = Integer.parseInt(userId);
		yourDAL.healthPoints = yourHealth;
		//update opponentHealthPoints
		opponentDAL.healthPoints = opponentHealth;
				
		System.out.println("YourHealth " + yourHealth);
		System.out.println("OpponetHealth " + opponentHealth);
		
		ListDTO<FightDataDAL> retSuccessDTO = new ListDTO<FightDataDAL>();
		List<FightDataDAL> retList = new ArrayList<>();
		retList.add(yourDAL);
		retList.add(opponentDAL);
		
		retSuccessDTO.success = true;
		retSuccessDTO.message = "success";
		retSuccessDTO.transferDataList = retList;
		return retSuccessDTO;
//		System.out.println("AHead " + aHead);
//		System.out.println("ABody " + aBody);
//		System.out.println("AArms " + aArms);
//		System.out.println("ALegs " + aLegs);
//		System.out.println("<---------------->");
//		System.out.println("DHead " + dHead);
//		System.out.println("DBody " + dBody);
//		System.out.println("DArms " + dArms);
//		System.out.println("DLegs " + dLegs);
//		
		
	}
	
	private int[] calculateDamage(ActionsDTO yourActions, ActionsDTO opponentActions) {
		
		int givenDamage = 0; 
		//how much damage you given
//		int givenDamage = (opponentActions.defenceHead - yourActions.attackHead) * CharacterBodyPart.HEAD.getDamagePoints()
//						+ (opponentActions.defenceBody - yourActions.attackBody) * CharacterBodyPart.BODY.getDamagePoints()
//						+ (opponentActions.defenceArms - yourActions.attackArms) * CharacterBodyPart.HANDS.getDamagePoints()
//						+ (opponentActions.defenceLegs - yourActions.attackLegs) * CharacterBodyPart.LEGS.getDamagePoints();
		
		if(yourActions.attackHead > 0) {
			givenDamage += (opponentActions.defenceHead - yourActions.attackHead) * CharacterBodyPart.HEAD.getDamagePoints();
		}
		if(yourActions.attackBody > 0) {
			givenDamage += (opponentActions.defenceBody - yourActions.attackBody) * CharacterBodyPart.BODY.getDamagePoints();
		}
		if(yourActions.attackArms > 0) {
			givenDamage += (opponentActions.defenceArms - yourActions.attackArms) * CharacterBodyPart.HANDS.getDamagePoints();
		}
		if(yourActions.attackLegs > 0) {
			givenDamage += (opponentActions.defenceLegs - yourActions.attackLegs) * CharacterBodyPart.LEGS.getDamagePoints();
		}
		
		int takenDamage = 0;
		
//		int takenDamage = (yourActions.defenceHead - opponentActions.attackHead) * CharacterBodyPart.HEAD.getDamagePoints()
//						+ (yourActions.defenceBody - opponentActions.attackBody) * CharacterBodyPart.BODY.getDamagePoints()
//						+ (yourActions.defenceArms - opponentActions.attackArms) * CharacterBodyPart.HANDS.getDamagePoints()
//						+ (yourActions.defenceLegs - opponentActions.attackLegs) * CharacterBodyPart.LEGS.getDamagePoints();
		
		if(opponentActions.attackHead > 0) {
			takenDamage += (yourActions.defenceHead - opponentActions.attackHead) * CharacterBodyPart.HEAD.getDamagePoints();
		}
		if(opponentActions.attackBody > 0) {
			takenDamage += (yourActions.defenceBody - opponentActions.attackBody) * CharacterBodyPart.BODY.getDamagePoints();
		}
		if(opponentActions.attackArms > 0) {
			takenDamage += (yourActions.defenceArms - opponentActions.attackArms) * CharacterBodyPart.HANDS.getDamagePoints();
		}
		if(opponentActions.attackLegs > 0) {
			takenDamage += (yourActions.defenceLegs - opponentActions.attackLegs) * CharacterBodyPart.LEGS.getDamagePoints();
		}
		System.out.println(givenDamage);
		System.out.println(takenDamage);
		int[] ret = {givenDamage, takenDamage};
		
		return ret;
	}
	
	
}
