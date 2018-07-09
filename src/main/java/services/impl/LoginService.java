package services.impl;
import models.business.User;
import models.business.UserLoginData;
import models.dto.UserFrontDTO;
import services.ILoginService;

public class LoginService implements ILoginService{
	
//	HigherLoginService hService;
//
//	public LoginService(HigherLoginService hServiceImpl) {
//		hService = hServiceImpl;
//	}
	@Override
	public UserFrontDTO login(UserLoginData userIn) {
		HigherLoginService hlog=new HigherLoginService();
		hlog.login(userIn);
		User userOut=new User();
		userOut.name=hlog.login(userIn)._userDal.name;
		userOut.userId=hlog.login(userIn)._userDal.userId;
		userOut.eMail=hlog.login(userIn)._userDal.eMail;
		
		
		
		return new UserFrontDTO(true, "success",userOut);
	}
//	public void arVeikia() {
//		System.out.println("veikia");
//	}


//	public boolean login(String userName, String pass) {
//		HigherLoginService hlogin = new HigherLoginService();
//		if (hlogin.login(userName, pass) == true) {
//			System.out.println("is hservice atejo leidimas");
//			return true;
//		}
//
//		return false;
//	}
}
