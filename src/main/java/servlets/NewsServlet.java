package servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.business.Player;
import models.constant.UserStatus;
import services.ICache;
import services.impl.CacheImpl;

@WebServlet(urlPatterns = "/News")
public class NewsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public NewsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		// response.getWriter().append("Served at: ").append(request.getContextPath());
		
		Player player = getThisPlayer(request);
	
		
		if(player.userStatus == UserStatus.PLAYING) {
			String url = "/JFight/setter?name=" + player.user.userId + "&fightId=" + 1;
			response.sendRedirect(url);
			
		} else {
			
			//Check GUID if user is valid, get his UserName
			Map<Integer, String> listPlayers = getReadyPlayers();
			request.setAttribute("readyPlayers", listPlayers);
			request.setAttribute("userName", player.user.name);
			
			
			String param = request.getParameter("button");
			if (param != null) {

				if (param.equals("refresh")) {
					request.getRequestDispatcher("News.jsp").forward(request, response);
				}
				
				// If player press Play button, check if player hasn't selected himself.
				if (param.equals("play") && player.user.userId != Integer.parseInt(request.getParameter("selectedPlayer"))) {
					
					ICache cache = CacheImpl.getInstance();
					cache.getPlayer(Integer.parseInt(request.getParameter("selectedPlayer"))).userStatus = UserStatus.PLAYING;
					player.userStatus = UserStatus.PLAYING;
					String url = "/JFight/setter?name=" + player.user.userId + "&fightId=" + 1;
					
					
					response.sendRedirect(url);
//					request.getRequestDispatcher("fight.jsp").forward(request, response);
					
				}
				else {
					player.userStatus = UserStatus.NOT_READY;
					request.getRequestDispatcher("News.jsp").forward(request, response);
				}
			}

			// from url parameter get value and convert it to boolean.
			else {
				Boolean ready = request.getParameter("ready") == null ? false : Boolean.valueOf(request.getParameter("ready"));
				
				if (ready == false) {
					System.out.println("ready=false");
					request.setAttribute("ReadyMessage", "YOU ARE NOT READY");
					player.userStatus = UserStatus.NOT_READY;
					request.getRequestDispatcher("News.jsp").forward(request, response);
					

				} else if(ready == true) {
					System.out.println("ready=true");
					request.setAttribute("ReadyMessage", "YOU ARE READY");
					player.userStatus = UserStatus.READY;
					request.getRequestDispatcher("News.jsp").forward(request, response);
				}
			}
			
			// }
			// doPost(request, response);
		}
		
		

	}

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
		return player;		//It can be null, then we get unhandeled error
	}

	private Map<Integer, String> getReadyPlayers() {
		ICache cache = CacheImpl.getInstance();
		Map<Integer, Player> players = cache.getPlayers();
		Map<Integer, String> listPlayers = new HashMap<>();
		
		for(Map.Entry<Integer, Player> entry : players.entrySet()) {
			if(entry.getValue().userStatus == UserStatus.READY) {
				listPlayers.put(entry.getValue().user.userId, entry.getValue().user.name);
			}
		}
		return listPlayers;
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.getRequestDispatcher("News.jsp").forward(request, response);

	}
	

	// TODO Auto-generated method stub
	// doGet(request, response);

}
