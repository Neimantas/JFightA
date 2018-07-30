package services.impl;

import java.util.List;

import models.dal.CharacterDAL;
import models.dto.ListDTO;
import services.ICRUD;
import services.IUserInfo;

public class UserInfoImpl implements IUserInfo {

	private ICRUD _crud;
	private List<CharacterDAL> _cDAL;

	public UserInfoImpl(CRUDImpl crud) {
		_crud = crud;
	}

	@Override
	public ListDTO<CharacterDAL> getUserInfo(int userId) {

		CharacterDAL dal = new CharacterDAL();

		dal.userId = userId;

		ListDTO<CharacterDAL> dto = _crud.<CharacterDAL>read(dal);
		if (dto.success) {

			_cDAL = dto.transferDataList;

			ListDTO<CharacterDAL> retSuccess = new ListDTO();
			retSuccess.success = true;
			retSuccess.message = "User exists in DB."; //Needs ENUM
			retSuccess.transferDataList = _cDAL;

			return retSuccess;
		}

		ListDTO<CharacterDAL> retFailure = new ListDTO();
		retFailure.success = false;
		retFailure.message = "Error! No such user."; //Needs ENUM

		return retFailure;
	}

}