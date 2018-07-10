package services.impl;

import models.business.User;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dal.UserDAL;
import models.dto.UserFrontDTO;
import services.ILoginService;

public class LoginService implements ILoginService {

	// HigherLoginService hService;
	//
	// public LoginService(HigherLoginService hServiceImpl) {
	// hService = hServiceImpl;
	// }
	@Override
	public UserFrontDTO login(UserLoginData userIn) {
		HigherLoginService hlog = new HigherLoginService();
		User userOut = new User();
		if (hlog.login(userIn)._success) {
			UserDAL userInDal = hlog.login(userIn)._userDal;
			userOut.userId = userInDal.userId;
			userOut.name = userInDal.name;
			userOut.eMail = userInDal.eMail;
			userOut.accessLevel = userInDal.accessLevel;
			return new UserFrontDTO(true, "success", userOut);
		}
		return new UserFrontDTO(false,hlog.login(userIn)._message,null);

	

		
	}

	@Override
	public UserFrontDTO registration(UserRegIn userRegIn) {
		
		return null;
	}

}
