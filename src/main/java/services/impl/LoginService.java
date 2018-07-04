package services.impl;

public class LoginService {
	public boolean login(String userName, String pass) {
		HigherLoginService hlogin = new HigherLoginService();
		if (hlogin.login(userName, pass) == true) {
			System.out.println("is hservice atejo leidimas");
			return true;
		}

		return false;
	}
}
