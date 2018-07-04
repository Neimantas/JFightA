package services.impl;

import services.IHigherLoginService;

public class HigherLoginService implements IHigherLoginService{

	public boolean login(String userName, String pass) {
		if(userName.equals("user1") && pass.equals("123")) {
			System.out.println("passvordas geras");
			return true;
		}
		if(userName!="user1" && pass!="123") {
			System.out.println("You shall not pass");
			return false;
		}
		return false;
	}

}
