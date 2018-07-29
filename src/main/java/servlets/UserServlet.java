package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configuration.StartupContainer;
import models.business.Player;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import services.ILogger;
import services.IUserInfo;
import services.impl.LoggerImpl;
import services.impl.UserInfoImpl;

@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IUserInfo _userInfo;
	private ILogger _logger;
	private Player _player;

	public UserServlet() {
		_userInfo = StartupContainer.easyDI.getInstance(UserInfoImpl.class);
		_logger = StartupContainer.easyDI.getInstance(LoggerImpl.class);
		_player = StartupContainer.easyDI.getInstance(Player.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// VALIDATOR
		// if(!_login.userValidator(request)) {
		// request.getRequestDispatcher("index.jsp").forward(request, response);
		// }

		ObjectDTO<Player> dtoPlayer = _userInfo.getCacheUserInfo(request);
		if (dtoPlayer.success) {
			_player = dtoPlayer.transferData;
		} else {
			// needs else return
		}

		if (request.getParameter("log") == "true" ? true : false) {
			ListDTO<String> dtoLog = _logger.getLogs(_player.characterInfo.userId, 2);
			if (dtoLog.success) {
				request.setAttribute("tableString", dtoLog.transferDataList);
			}
		}

		request.setAttribute("userName", firstLetterToUpper(_player.user.name));
		request.setAttribute("userId", _player.characterInfo.userId);
		request.setAttribute("level", _player.characterInfo.level);
		request.setAttribute("experience", _player.characterInfo.experience);
		request.setAttribute("healthPoints", _player.characterInfo.healthPoints);
		request.setAttribute("strenght", _player.characterInfo.strenght);
		request.setAttribute("attackItem", _player.characterInfo.attackItemId);
		request.setAttribute("defenceItem", _player.characterInfo.defenceItemId);

		request.getRequestDispatcher("user.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String firstLetterToUpper(String str) {
		str = str.substring(0, 1).toUpperCase() + str.substring(1);

		return str;
	}

}
