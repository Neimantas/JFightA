package services.impl;

import models.dal.UserDAL;
import models.dto.UserDTO;
import services.IHigherLoginService;

public class HigherLoginService implements IHigherLoginService {

	@Override
	public UserDTO login(UserDAL userDal) {

		return new UserDTO(true, "success", null);
	}

	// public boolean login(String userName, String pass) {
	// if(userName.equals("user1") && pass.equals("123")) {
	// System.out.println("passvordas geras");
	// return true;
	// }
	// if(userName!="user1" && pass!="123") {
	// System.out.println("You shall not pass");
	// return false;
	// }
	// return false;
	// }

}
