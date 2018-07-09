package services.impl;

import models.business.UserLoginData;
import models.dal.UserDAL;
import models.dto.UserDTO;
import services.IHigherLoginService;

public class HigherLoginService implements IHigherLoginService {

	CRUDImpl crud;

	public HigherLoginService() {
		crud = new CRUDImpl(new DatabaseImpl());
	}

	@Override
	public UserDTO login(UserLoginData userIn) {
		UserDAL userInDal = new UserDAL();
		userInDal.name = userIn.name;
		userInDal.password = userIn.password;
		UserDAL userDal = crud.read(userInDal).transferDataList.get(0);
//		UserDAL userDal = new UserDAL();
//		userDal = (UserDAL) userOut;

		return new UserDTO(true, "success", userDal);
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
