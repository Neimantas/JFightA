package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.dal.CharacterDAL;
import models.dal.FightDataDAL;
import models.dto.ListDTO;
import services.ILogger;
import services.IUserInfo;
import services.impl.LoggerImpl;
import services.impl.UserInfoImpl;

@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IUserInfo _userInfo;
	private String _userId;
	private CharacterDAL _character;
	private ILogger _logger;

	public UserServlet() {
		super();
		_userInfo = new UserInfoImpl();
		_character = new CharacterDAL();
		_logger = new LoggerImpl();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		_userId = request.getParameter("userId");

		ListDTO<CharacterDAL> dtoCharacter = _userInfo.getUserInfo(Integer.parseInt(_userId));
		if (dtoCharacter.success) {
			_character = dtoCharacter.transferDataList.get(0);
		} else {
			// needs else return
		}

		ListDTO<String> dtoLog = _logger.getLogs(_character.userId, 2);
		if (dtoLog.success) {
			request.setAttribute("tableString", dtoLog.transferDataList);
		}

		request.setAttribute("userId", _character.userId);
		request.setAttribute("level", _character.level);
		request.setAttribute("experience", _character.experience);
		request.setAttribute("healthPoints", _character.healthPoints);
		request.setAttribute("strenght", _character.strenght);
		request.setAttribute("attackItem", _character.attackItemId);
		request.setAttribute("defenceItem", _character.defenceItemId);

		request.getRequestDispatcher("user.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
