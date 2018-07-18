package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.protocol.Protocol.GetProfilerEventHandlerInstanceFunction;

import models.dal.CharacterDAL;
import models.dal.ImageDAL;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.IImage;
import services.IUserInfo;
import services.impl.ImageImpl;
import services.impl.UserInfoImpl;

@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IUserInfo _userInfo;
	private String _userId;
	private CharacterDAL _character;
	private IImage _image;

	public UserServlet() {
		super();
		_userInfo = new UserInfoImpl();
		_character = new CharacterDAL();
		_image = ImageImpl.getInstance();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		_userId = request.getParameter("userId");
		
		ListDTO<CharacterDAL> dtoCharacter = _userInfo.getUserInfo(Integer.parseInt(_userId));
		if(dtoCharacter.success) {
			_character = dtoCharacter.transferDataList.get(0);
		} else {
			//needs else return
		}
		
		ObjectDTO<ImageDAL> dtoImage = _image.getImage(Integer.parseInt(_userId));
				
		request.setAttribute("image", _image.getImage(Integer.parseInt(_userId)));
		request.setAttribute("userId", _character.userId);
		request.setAttribute("level", _character.level);
		request.setAttribute("experience", _character.experience);
		request.setAttribute("healthPoints", _character.healthPoints);
		request.setAttribute("strenght", _character.strenght);
		
		request.getRequestDispatcher("user.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
