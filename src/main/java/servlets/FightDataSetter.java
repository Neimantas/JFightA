package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configuration.StartupContainer;
import models.business.Player;
import services.ICache;
import services.ILoginService;
import services.impl.CacheImpl;
import services.impl.LoginService;

/**
 * Servlet implementation class FightDataSetter
 */
@WebServlet("/setter")
public class FightDataSetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ILoginService _logService;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FightDataSetter() {
        super();
        _logService = StartupContainer.easyDI.getInstance(LoginService.class);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//get player instance to set health points
		
		if(!_logService.userValidator(request, response)) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		
		ICache cache = CacheImpl.getInstance();
		Player player = cache.getPlayer(Integer.parseInt(request.getParameter("name")));
		
		Cookie userIdCookie = new Cookie("userId", request.getParameter("name"));
		Cookie fightIdCookie = new Cookie("fightId", request.getParameter("fightId"));
		Cookie healthCookie = new Cookie("health", Integer.toString(player.characterInfo.healthPoints));
		Cookie roundCookie = new Cookie("round", "0");
		userIdCookie.setMaxAge(60 * 60 * 24); //24h
		fightIdCookie.setMaxAge(60 * 60 * 24);
		response.addCookie(userIdCookie);
		response.addCookie(fightIdCookie);
		response.addCookie(healthCookie);
		response.addCookie(roundCookie);
		
//		request.getRequestDispatcher("/fight").forward(request, response);
		response.sendRedirect("/JFight/fight");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
