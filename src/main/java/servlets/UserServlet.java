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
import services.ILoginService;
import services.IUserInfo;
import services.impl.LoggerImpl;
import services.impl.LoginService;
import services.impl.UserInfoImpl;

@WebServlet(urlPatterns = "/user")
public class UserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IUserInfo _userInfo;
	private ILogger _logger;
	private ILoginService _login;

	public UserServlet() {
		_userInfo = StartupContainer.easyDI.getInstance(UserInfoImpl.class);
		_logger = StartupContainer.easyDI.getInstance(LoggerImpl.class);
		_login = StartupContainer.easyDI.getInstance(LoginService.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Player _player = StartupContainer.easyDI.getInstance(Player.class);

		String userId = request.getParameter("userId");

		userValidator(request, response);

		_player = showLoggedUsersInfo(request, _player, userId);

		showOtherUsersInfoAndLog(request, _player, userId);

		request.setAttribute("currentUserName", _player.user.name);

		request.getRequestDispatcher("user.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void showOtherUsersInfoAndLog(HttpServletRequest request, Player _player, String userId) {
		if (request.getParameter("log") == null && userId != null) {
			ObjectDTO<Player> dtoUser = _userInfo.getUserInfo(Integer.parseInt(userId));
			if (dtoUser.success) {
				Player user = dtoUser.transferData;

				request.setAttribute("userName", user.user.name);
				request.setAttribute("userId", user.user.userId);
				request.setAttribute("level", user.characterInfo.level);
				request.setAttribute("experience", user.characterInfo.experience);
				request.setAttribute("healthPoints", user.characterInfo.healthPoints);
				request.setAttribute("strenght", user.characterInfo.strenght);
				request.setAttribute("attackItem", user.characterInfo.attackItemId);
				request.setAttribute("defenceItem", user.characterInfo.defenceItemId);
			}

			ListDTO<String> dtoLog = _logger.getLogs(_player.characterInfo.userId, Integer.parseInt(userId));
			if (dtoLog.success) {
				request.setAttribute("tableString", dtoLog.transferDataList);
			}
		}
	}

	private Player showLoggedUsersInfo(HttpServletRequest request, Player _player, String userId) {
		ObjectDTO<Player> dtoPlayer = _userInfo.getLoggedUserInfo(request);
		if (dtoPlayer.success) {
			_player = dtoPlayer.transferData;
		}

		if (userId == null) {
			request.setAttribute("userName", _player.user.name);
			request.setAttribute("userId", _player.characterInfo.userId);
			request.setAttribute("level", _player.characterInfo.level);
			request.setAttribute("experience", _player.characterInfo.experience);
			request.setAttribute("healthPoints", _player.characterInfo.healthPoints);
			request.setAttribute("strenght", _player.characterInfo.strenght);
			request.setAttribute("attackItem", _player.characterInfo.attackItemId);
			request.setAttribute("defenceItem", _player.characterInfo.defenceItemId);
		}
		return _player;
	}

	private void userValidator(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!_login.userValidator(request, response)) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}
}
