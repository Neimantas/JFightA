package services.impl;
import models.business.User;
import models.dto.UserFrontDTO;
import services.ILoginService;

public class LoginService implements ILoginService{

	@Override
	public UserFrontDTO login(User user) {
		HigherLoginService hlogin = new HigherLoginService();
		
		return null;
	}


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
