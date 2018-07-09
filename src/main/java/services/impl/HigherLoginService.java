package services.impl;


import models.business.UserLoginData;
import models.dal.UserDAL;
import models.dto.UserDTO;
import services.IHigherLoginService;

public class HigherLoginService implements IHigherLoginService {

	CRUDImpl crud;
	public HigherLoginService() {
		crud =new CRUDImpl(new DatabaseImpl());
	}
	

	
	@Override
	public UserDTO login(UserLoginData userIn) {
		System.out.println("Pateko");
		UserDAL user2=new UserDAL();
		user2.name=userIn.name;
		user2.password=userIn.password;
		Object user1=crud.read(user2).transferDataList.get(0);
		UserDAL userDal=new UserDAL();
		userDal=(UserDAL)user1;
		
//		System.out.println("pavyko gauti id = "+userDal.userId);
		

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
