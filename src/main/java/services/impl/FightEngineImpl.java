package services.impl;

import java.util.ArrayList;
import java.util.List;

import models.constant.DefaultDamagePoints;
import models.dal.FightDataDAL;
import models.dto.ActionsDTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IFightEngine;

public class FightEngineImpl implements IFightEngine {
	
	private ICRUD _crud;

	public FightEngineImpl(CRUDImpl crud) {
		_crud = crud;
	}
	
	@Override
	public ObjectDTO<FightDataDAL> getOpponentData(int fightId, String userID) {
		//This Method is not used. (First try)
		
		//temporary solution, only for testing purposes
		int userId = Integer.parseInt(userID);
		//
		FightDataDAL dal = new FightDataDAL();
		dal.fightId = fightId;
		ListDTO<FightDataDAL> ldto = _crud.<FightDataDAL>read(dal);
		if(ldto.success) {
			int counter = 0;
			while(ldto.transferDataList.size() < 2 && counter < 10000) {
				ldto = _crud.<FightDataDAL>read(dal);
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
		//This method is in Use :)
		//temporary solution, only for testing purposes
		int userId = Integer.parseInt(userID); 											//Parse from string.
		//
		FightDataDAL dal = new FightDataDAL();
		dal.fightId = fightId;
		System.out.println("********************************");
		System.out.println(fightId);
		System.out.println(dal);
		System.out.println("********************************");
		ListDTO<FightDataDAL> ldto = _crud.<FightDataDAL>read(dal);						//by this DAL search for fight ID made in CRUD
		if(ldto.success) {
//			int counter = 0;
//			while(ldto.transferDataList.size() < 2 && counter < 10000) {
//				ldto = crud.<FightDataDAL>read(dal);
//				counter++;
//			}
			
			List<FightDataDAL> data = ldto.transferDataList;							//Data from DB
			FightDataDAL retDAL = null;
			for(FightDataDAL d : data) {												//Searching for existing Round ID
				if(d.userId != userId && d.round == round) {
					retDAL = d;
					break;
				}
			}	
			if (retDAL == null) {														//If Round ID not found - return DAL with success = false 
				ObjectDTO<FightDataDAL> retFailure = new ObjectDTO<>();
				retFailure.success = false;
				retFailure.message = "Cannot find opponent";
				return retFailure;
			}
				
			ObjectDTO<FightDataDAL> retSuccess = new ObjectDTO<FightDataDAL>();			//If Round is found - fill data to ObjectDTO
			retSuccess.success = true;
			retSuccess.message = "message";
			retSuccess.transferData = retDAL;
			return retSuccess;
			}
			
		
		ObjectDTO<FightDataDAL> retFailure = new ObjectDTO<>();							//If CRUD makes fail - ObjectTDO filled with success - false.
		retFailure.success = false;
		retFailure.message = ldto.message;
		return retFailure;
		
	}

	@Override
	public ListDTO<FightDataDAL> engine(int fightId, int roundId, int health, String userId, ActionsDTO yourAction) {
		
		FightDataDAL insertDAL = new FightDataDAL();									//Fill Users darta to FightData DB.
		insertDAL.fightId = fightId;
		insertDAL.round = roundId;
		insertDAL.userId = Integer.parseInt(userId); //temporary solution
		System.out.println("################");
		System.out.println(userId + " " + fightId);
		System.out.println("################");
		insertDAL.healthPoints = health;
		insertDAL.attackHead = yourAction.attackHead;
		insertDAL.attackBody = yourAction.attackBody;
		insertDAL.attackHands = yourAction.attackArms;
		insertDAL.attackLegs = yourAction.attackLegs;
		insertDAL.defenceHead = yourAction.defenceHead;
		insertDAL.defenceBody = yourAction.defenceBody;
		insertDAL.defenceHands = yourAction.defenceArms;
		insertDAL.defenceLegs = yourAction.defenceLegs;
		
		_crud.<FightDataDAL>create(insertDAL);											//Insert Data to FightData. Need to make check if Successfull
		
		int counter = 0;
		ObjectDTO<FightDataDAL> obj = getOpponentData(fightId, roundId, userId);
		while(counter < 1000) {															//Loop witch is waiting for users input. 30sec waiting solution made in frontend
			if(!obj.success && obj.message.equals("Cannot find opponent")) {			//needs upgrade, if data not received - autoWin for waiting user.
				counter++;																//needs upgrade, instead of magic number count, make 35 second count/loop.
				obj = getOpponentData(fightId, roundId, userId);
//				System.out.println("In waiting loop...");
			} else {
				break;
			}
		}
		 
		if(!obj.success && obj.message.equals("Cannot find opponent"))	{				//When Data is nor received from one of the Users.
			System.out.println("ERROR: " + obj.message);
			System.out.println("Here current user action data will be written");
			System.out.println("");
			ListDTO<FightDataDAL> ret = new ListDTO<FightDataDAL>();
			ret.success = false;
			ret.message = obj.message;
			return ret;
		}
			
		
		
		
		FightDataDAL opponentDAL = obj.transferData;									//Fill data to DAL when all data received successfull. DAL for sending to servlet.
		ActionsDTO opponentActions = new ActionsDTO();
		
		opponentActions.attackHead = opponentDAL.attackHead == null?0:opponentDAL.attackHead;		//if form DB getting info with null - make it "0".
		opponentActions.attackBody = opponentDAL.attackBody == null?0:opponentDAL.attackBody;
		opponentActions.attackArms = opponentDAL.attackHands == null?0:opponentDAL.attackHands;
		opponentActions.attackLegs = opponentDAL.attackLegs == null?0:opponentDAL.attackLegs;
		
		opponentActions.defenceHead = opponentDAL.defenceHead == null?0:opponentDAL.defenceHead;
		opponentActions.defenceBody = opponentDAL.defenceBody == null?0:opponentDAL.defenceBody;
		opponentActions.defenceArms = opponentDAL.defenceHands == null?0:opponentDAL.defenceHands;
		opponentActions.defenceLegs = opponentDAL.defenceLegs == null?0:opponentDAL.defenceLegs;
		
		int[] damages = calculateDamage(yourAction, opponentActions);					//CalculateDamage :)
		
		int yourHealth = health + damages[1];											//sending these to Servlet
		int opponentHealth = opponentDAL.healthPoints + damages[0];
		
		FightDataDAL yourDAL = new FightDataDAL();										//to send DAL to Servlet
		yourDAL.fightId = fightId;
		yourDAL.round = roundId;
		yourDAL.userId = Integer.parseInt(userId);
		yourDAL.healthPoints = yourHealth;
		//update opponentHealthPoints
		opponentDAL.healthPoints = opponentHealth;										//to show oponents health.
				
		System.out.println("YourHealth " + yourHealth);
		System.out.println("OpponetHealth " + opponentHealth);
		
		ListDTO<FightDataDAL> retSuccessDTO = new ListDTO<FightDataDAL>();
		List<FightDataDAL> retList = new ArrayList<>();
		retList.add(yourDAL);
		retList.add(opponentDAL);
		
		retSuccessDTO.success = true;
		retSuccessDTO.message = "success";
		retSuccessDTO.transferDataList = retList;
		return retSuccessDTO;															//send Info to Servlet.
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
		//Calculating damage of round's.
		
		int givenDamage = 0; 
		//how much damage you given
//		int givenDamage = (opponentActions.defenceHead - yourActions.attackHead) * CharacterBodyPart.HEAD.getDamagePoints()
//						+ (opponentActions.defenceBody - yourActions.attackBody) * CharacterBodyPart.BODY.getDamagePoints()
//						+ (opponentActions.defenceArms - yourActions.attackArms) * CharacterBodyPart.HANDS.getDamagePoints()
//						+ (opponentActions.defenceLegs - yourActions.attackLegs) * CharacterBodyPart.LEGS.getDamagePoints();
		
		if(yourActions.attackHead > 0) {
			givenDamage += (opponentActions.defenceHead - yourActions.attackHead) * DefaultDamagePoints.HEAD.getDamagePoints();
		}
		if(yourActions.attackBody > 0) {
			givenDamage += (opponentActions.defenceBody - yourActions.attackBody) * DefaultDamagePoints.BODY.getDamagePoints();
		}
		if(yourActions.attackArms > 0) {
			givenDamage += (opponentActions.defenceArms - yourActions.attackArms) * DefaultDamagePoints.HANDS.getDamagePoints();
		}
		if(yourActions.attackLegs > 0) {
			givenDamage += (opponentActions.defenceLegs - yourActions.attackLegs) * DefaultDamagePoints.LEGS.getDamagePoints();
		}
		
		int takenDamage = 0;
		
//		int takenDamage = (yourActions.defenceHead - opponentActions.attackHead) * CharacterBodyPart.HEAD.getDamagePoints()
//						+ (yourActions.defenceBody - opponentActions.attackBody) * CharacterBodyPart.BODY.getDamagePoints()
//						+ (yourActions.defenceArms - opponentActions.attackArms) * CharacterBodyPart.HANDS.getDamagePoints()
//						+ (yourActions.defenceLegs - opponentActions.attackLegs) * CharacterBodyPart.LEGS.getDamagePoints();
		
		if(opponentActions.attackHead > 0) {
			takenDamage += (yourActions.defenceHead - opponentActions.attackHead) * DefaultDamagePoints.HEAD.getDamagePoints();
		}
		if(opponentActions.attackBody > 0) {
			takenDamage += (yourActions.defenceBody - opponentActions.attackBody) * DefaultDamagePoints.BODY.getDamagePoints();
		}
		if(opponentActions.attackArms > 0) {
			takenDamage += (yourActions.defenceArms - opponentActions.attackArms) * DefaultDamagePoints.HANDS.getDamagePoints();
		}
		if(opponentActions.attackLegs > 0) {
			takenDamage += (yourActions.defenceLegs - opponentActions.attackLegs) * DefaultDamagePoints.LEGS.getDamagePoints();
		}
		System.out.println(givenDamage);
		System.out.println(takenDamage);
		int[] ret = {givenDamage, takenDamage};
		
		return ret;
	}
	
	
}
