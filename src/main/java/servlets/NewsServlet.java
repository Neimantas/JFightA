package servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configuration.StartupContainer;
import models.business.Player;
import models.constant.UserStatus;
import services.ICache;
import services.impl.CacheImpl;
import services.impl.HigherService;
import services.impl.LoginService;

@WebServlet(urlPatterns = "/News")
public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private LoginService _logService;
	private HigherService _hService;

	public NewsServlet() {
		super();
		_logService = StartupContainer.easyDI.getInstance(LoginService.class);
		_hService=StartupContainer.easyDI.getInstance(HigherService.class);

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		userValidation(request, response);
		Player player = getThisPlayer(request);
		// If current player status is playing - redirect him to fight.jsp. News.jsp
		// makes autoRefresh every 5 seconds.
		if (player.userStatus == UserStatus.PLAYING) {
			movePlayerToFight(response, player);
		}

		// If player Is not playing and not moved to Fight, execute other stuff.
		else {
			Map<Integer, String> listPlayers = getReadyPlayers();
			request.setAttribute("readyPlayers", listPlayers);
			request.setAttribute("userName", player.user.name);
			buttonsHandler(request, response);
		}

	}

	private void buttonsHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Player player = getThisPlayer(request);
		String param = request.getParameter("button");
		// Check if there is params in url.
		if (param != null) {
			// refresh page on demand.
			if (param.equals("refresh")) {
				request.getRequestDispatcher("News.jsp").forward(request, response);
			}

			// If player press Play button, check if player hasn't selected himself
			if (param.equals("play")
					&& player.user.userId != Integer.parseInt(request.getParameter("selectedPlayer"))) {

				playButtonHandler(request, response, player);
				
				// On first page load there is no params, so
				// If there is no params, set player as notReady.
			} else {
				player.userStatus = UserStatus.NOT_READY;
				request.getRequestDispatcher("News.jsp").forward(request, response);
			}
		}

		
		else {
			readyMessageHandler(request, response);
		}
	}

	private void movePlayerToFight(HttpServletResponse response, Player player) throws IOException {
		Integer currentFight = player.currentFightID;
		String url = "/JFight/setter?name=" + player.user.userId + "&fightId=" + currentFight;
		response.sendRedirect(url);
	}

	private void playButtonHandler(HttpServletRequest request, HttpServletResponse response,
			Player player) throws IOException {
		ICache cache = CacheImpl.getInstance();
		// After Play button is pressed, set remote player from list status as playing.
		cache.getPlayer(
				Integer.parseInt(request.getParameter("selectedPlayer"))).userStatus = UserStatus.PLAYING;
		// set current player as playing.
		player.userStatus = UserStatus.PLAYING;

		// Get unused fightID
		Integer lastFightID = _hService.getNewFightId();
		// Set in what FightID should remote player be transfered.
		cache.getPlayer(
				Integer.parseInt(request.getParameter("selectedPlayer"))).currentFightID = lastFightID;
		// Set and Redirect current player to Fight.jsp with FightID.
		String url = "/JFight/setter?name=" + player.user.userId + "&fightId=" + lastFightID;
		response.sendRedirect(url);
	}

	private void readyMessageHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Player player = getThisPlayer(request);
		// Checking Player status and writing message and button text accordingly.
		Boolean ready = request.getParameter("ready") == null ? false
				: Boolean.valueOf(request.getParameter("ready"));

		if (ready == false) {
			request.setAttribute("ReadyMessage", "YOU ARE NOT READY");
			player.userStatus = UserStatus.NOT_READY;
			request.getRequestDispatcher("News.jsp").forward(request, response);

			// Skip this step if player is set to play, in this case player can't set no
			// ready and will be redirected to fight engine.
		} else if (ready == true && player.userStatus != UserStatus.PLAYING) {
			request.setAttribute("ReadyMessage", "YOU ARE READY");
			player.userStatus = UserStatus.READY;
			request.getRequestDispatcher("News.jsp").forward(request, response);
		}
	}

	private void userValidation(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (!_logService.userValidator(request)) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
	}

	// Get current player Id from his cookie.
	private Player getThisPlayer(HttpServletRequest request) {
		ICache cache = CacheImpl.getInstance();
		Cookie[] cookies = request.getCookies();
		String cookieValue = "";
		Player player = new Player();
		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals("JFightUser")) {
				cookieValue = cookies[i].getValue();
			}
		}
		for (Entry<Integer, Player> entry : cache.getPlayers().entrySet()) {
			if (entry.getValue().user.cookiesValue.equals(cookieValue)) {
				return cache.getPlayer(entry.getValue().user.userId);
			}
		}
		return player; // It must be not null, then we get unhandeled error
	}

	// Get all player who status is ready and put them to HashMap.
	private Map<Integer, String> getReadyPlayers() {
		ICache cache = CacheImpl.getInstance();
		Map<Integer, Player> players = cache.getPlayers();
		Map<Integer, String> listPlayers = new HashMap<>();

		// Run throw every player online, checking their status, if status is ready, put
		// to Map.
		for (Map.Entry<Integer, Player> entry : players.entrySet()) {
			if (entry.getValue().userStatus == UserStatus.READY) {
				listPlayers.put(entry.getValue().user.userId, entry.getValue().user.name);
			}
		}
		return listPlayers;
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("News.jsp").forward(request, response);
	}
}
