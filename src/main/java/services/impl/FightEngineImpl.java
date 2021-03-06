package services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import models.business.Actions;
import models.business.FightData;
import models.business.Item;
import models.constant.DefaultDamagePoints;
import models.constant.Error;
import models.constant.ItemType;
import models.constant.Settings;
import models.constant.Success;
import models.constant.TimeMs;
import models.dal.FightDataDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IFightEngine;
import services.IItem;

public class FightEngineImpl implements IFightEngine {
	
	private ICRUD _crud;
	private IItem _item;

	public FightEngineImpl(CRUDImpl crud, ItemImpl item) {
		_crud = crud;
		_item = item;
	}
	
	public ObjectDTO<FightDataDAL> getOpponentData(int fightId, int round, int userID) {
		int userId = userID; 											
		FightDataDAL dal = new FightDataDAL();
		dal.fightId = fightId;
		ListDTO<FightDataDAL> ldto = _crud.<FightDataDAL>read(dal);						//by this DAL search for fight ID made in CRUD
		if(ldto.success) {
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
				retFailure.message = Error.OPPONENT_IS_MISSING.getMessage();
				return retFailure;
			}
			ObjectDTO<FightDataDAL> retSuccess = new ObjectDTO<FightDataDAL>();			//If Round is found - fill data to ObjectDTO
			retSuccess.success = true;
			retSuccess.transferData = retDAL;
			return retSuccess;
			}
		ObjectDTO<FightDataDAL> retFailure = new ObjectDTO<>();							//If CRUD makes fail - ObjectTDO filled with success - false.
		retFailure.success = false;
		retFailure.message = ldto.message;
		return retFailure;
	}

	@Override
	public ListDTO<FightDataDAL> engine(FightData fightData, Actions yourAction) {
		//Insert Data to FightData. Need to make check if Successfull
		insertData(fightData, yourAction);
		long waitForOtherUserAction = System.currentTimeMillis() 
				+ Settings.PLAYER_ACTION_WAITING_TIME * TimeMs.SECOND.getMilliseconds();
		ObjectDTO<FightDataDAL> obj = getOpponentData(fightData.fightId, fightData.round, 
				fightData.playerAUserId);
		
		while(System.currentTimeMillis() < waitForOtherUserAction) {															//Loop witch is waiting for users input. 30sec waiting solution made in frontend
			if(!obj.success && obj.message.equals(Error.OPPONENT_IS_MISSING.getMessage())) {			//needs upgrade, if data not received - autoWin for waiting user.
				obj = getOpponentData(fightData.fightId, fightData.round, fightData.playerAUserId);
			} else {
				break;
			}
		}
		if(!obj.success && obj.message.equals(Error.OPPONENT_IS_MISSING.getMessage()))	{				//When Data is nor received from one of the Users.
			ListDTO<FightDataDAL> ret = new ListDTO<FightDataDAL>();
			ret.success = false;
			ret.message = obj.message;
			return ret;
		}
		FightDataDAL opponentDAL = obj.transferData;									//Fill data to DAL when all data received successfull. DAL for sending to servlet.
		Actions opponentActions = getOpponentActions(opponentDAL);
		int[] damages = calculateDamage(yourAction, opponentActions, fightData.playerAUserId, opponentDAL.userId); //CalculateDamage :)
		FightDataDAL yourDAL = new FightDataDAL();										//yourDAL is only needed transfer yourHeath to Servlet
		//update players healthPoints
		yourDAL.healthPoints = fightData.playerAHealth - damages[1];
		opponentDAL.healthPoints = opponentDAL.healthPoints - damages[0];										//to show oponents health.
		//build success dto		
		ListDTO<FightDataDAL> retSuccessDTO = new ListDTO<FightDataDAL>();
		List<FightDataDAL> retList = new ArrayList<>();
		retList.add(yourDAL);
		retList.add(opponentDAL);
		retSuccessDTO.success = true;
		retSuccessDTO.message = Success.SUCCESS.getMessage();
		retSuccessDTO.transferDataList = retList;
		return retSuccessDTO;															//send Info to Servlet.
	}
	
	private int[] calculateDamage(Actions yourActions, Actions opponentActions, int yourUserId, int opponentUserId) {
		//Calculating damage of round's.
		Map<ItemType, Item> yourItems = _item.getUserItems(yourUserId);
		Map<ItemType, Item> opponentItems = _item.getUserItems(opponentUserId);
		int givenDamage = 0;
		
		if (yourActions.attackHead > 0) {
			givenDamage += countDamage(yourActions.attackHead, opponentActions.defenceHead, DefaultDamagePoints.HEAD.getDamagePoints(), 
					yourItems.get(ItemType.ATTACK).attackPoints, opponentItems.get(ItemType.DEFENCE).defencePoints);
		}
		if (yourActions.attackBody > 0) {
			givenDamage += countDamage(yourActions.attackBody, opponentActions.defenceBody, DefaultDamagePoints.BODY.getDamagePoints(), 
					yourItems.get(ItemType.ATTACK).attackPoints, opponentItems.get(ItemType.DEFENCE).defencePoints);
		}
		if (yourActions.attackArms > 0) {
			givenDamage += countDamage(yourActions.attackArms, opponentActions.defenceArms, DefaultDamagePoints.ARMS.getDamagePoints(), 
					yourItems.get(ItemType.ATTACK).attackPoints, opponentItems.get(ItemType.DEFENCE).defencePoints);
		}
		if (yourActions.attackLegs > 0) {
			givenDamage += countDamage(yourActions.attackLegs, opponentActions.defenceLegs, DefaultDamagePoints.LEGS.getDamagePoints(), 
					yourItems.get(ItemType.ATTACK).attackPoints, opponentItems.get(ItemType.DEFENCE).defencePoints);
		}
		
		int takenDamage = 0;
		
		if (opponentActions.attackHead > 0) {
			takenDamage += countDamage(opponentActions.attackHead, yourActions.defenceHead, DefaultDamagePoints.HEAD.getDamagePoints(), 
					opponentItems.get(ItemType.ATTACK).attackPoints, yourItems.get(ItemType.DEFENCE).defencePoints);
		}
		if (opponentActions.attackBody > 0) {
			takenDamage += countDamage(opponentActions.attackBody, yourActions.defenceBody, DefaultDamagePoints.BODY.getDamagePoints(), 
					opponentItems.get(ItemType.ATTACK).attackPoints, yourItems.get(ItemType.DEFENCE).defencePoints);
		}
		if (opponentActions.attackArms > 0) {
			takenDamage += countDamage(opponentActions.attackArms, yourActions.defenceArms, DefaultDamagePoints.ARMS.getDamagePoints(), 
					opponentItems.get(ItemType.ATTACK).attackPoints, yourItems.get(ItemType.DEFENCE).defencePoints);
		}
		if (opponentActions.attackLegs > 0) {
			takenDamage += countDamage(opponentActions.attackLegs, yourActions.defenceLegs, DefaultDamagePoints.LEGS.getDamagePoints(), 
					opponentItems.get(ItemType.ATTACK).attackPoints, yourItems.get(ItemType.DEFENCE).defencePoints);
		}
		int[] ret = {givenDamage, takenDamage};
		return ret;
	}
	
	private int countDamage(int attackAction, int defenceAction, int defaultDamagePoint, int itemAttackPoints, int itemDefencePoints) {
		if (attackAction - defenceAction == 1) {
			return defaultDamagePoint + itemAttackPoints;
		} else {
			return itemAttackPoints - itemDefencePoints;
		}
	}
	
	private void insertData(FightData fightData, Actions yourAction) {
		FightDataDAL insertDAL = new FightDataDAL();									//Fill Users darta to FightData DB.
		insertDAL.fightId = fightData.fightId;
		insertDAL.round = fightData.round;
		insertDAL.userId = fightData.playerAUserId;
		insertDAL.healthPoints = fightData.playerAHealth;
		insertDAL.attackHead = yourAction.attackHead;
		insertDAL.attackBody = yourAction.attackBody;
		insertDAL.attackArms = yourAction.attackArms;
		insertDAL.attackLegs = yourAction.attackLegs;
		insertDAL.defenceHead = yourAction.defenceHead;
		insertDAL.defenceBody = yourAction.defenceBody;
		insertDAL.defenceArms = yourAction.defenceArms;
		insertDAL.defenceLegs = yourAction.defenceLegs;
		
		_crud.<FightDataDAL>create(insertDAL);	
	}
	
	private Actions getOpponentActions(FightDataDAL opponentDAL) {
		Actions opponentActions = new Actions();
		opponentActions.attackHead = opponentDAL.attackHead == null?0:opponentDAL.attackHead;		//if form DB getting info with null - make it "0".
		opponentActions.attackBody = opponentDAL.attackBody == null?0:opponentDAL.attackBody;
		opponentActions.attackArms = opponentDAL.attackArms == null?0:opponentDAL.attackArms;
		opponentActions.attackLegs = opponentDAL.attackLegs == null?0:opponentDAL.attackLegs;
		opponentActions.defenceHead = opponentDAL.defenceHead == null?0:opponentDAL.defenceHead;
		opponentActions.defenceBody = opponentDAL.defenceBody == null?0:opponentDAL.defenceBody;
		opponentActions.defenceArms = opponentDAL.defenceArms == null?0:opponentDAL.defenceArms;
		opponentActions.defenceLegs = opponentDAL.defenceLegs == null?0:opponentDAL.defenceLegs;
		return opponentActions;
	}
}
