package services.impl;

import java.util.ArrayList;
import java.util.List;

import models.dal.CharacterDAL;
import models.dal.FightDataDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ICRUD;
import services.IUserInfo;

public class UserInfoImpl implements IUserInfo {
	
	private ICRUD crud;
	
	public UserInfoImpl(){
		crud = new CRUDImpl(new DatabaseImpl());
	}

	@Override
	public ListDTO<CharacterDAL> getUserInfo(int userId) {
		
		CharacterDAL dal = new CharacterDAL();
		
		dal.userId = userId;
		
		ListDTO<CharacterDAL> dto = crud.<CharacterDAL>read(dal);
		if(dto.success) {
			List<CharacterDAL> retList = new ArrayList<>();
			List<CharacterDAL> list = dto.transferDataList;
			for(CharacterDAL c : list) {
				
				CharacterDAL ret = new CharacterDAL();
				
				ret.userId = c.userId;
				ret.healthPoints = c.healthPoints;
				ret.strenght = c.strenght;
				ret.experience = c.experience;
				ret.level = c.level;
				ret.attackItemId = c.attackItemId;
				ret.defenceItemId = c.defenceItemId;
				
				retList.add(ret);
			}
			
			ListDTO<CharacterDAL> retSuccess = new ListDTO();
			retSuccess.success = true;
			retSuccess.message = "User exists in DB.";
			retSuccess.transferDataList = retList;

			return retSuccess;
			
		}
		
		ListDTO<CharacterDAL> retFailure = new ListDTO();
		retFailure.success = false;
		retFailure.message = "Error! No such user.";

		return retFailure;
	}

}
