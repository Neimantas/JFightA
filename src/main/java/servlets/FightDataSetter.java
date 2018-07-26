package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FightDataSetter
 */
@WebServlet("/setter")
public class FightDataSetter extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FightDataSetter() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		Cookie userIdCookie = new Cookie("userId", request.getParameter("name"));
		Cookie fightIdCookie = new Cookie("fightId", request.getParameter("fightId"));
		Cookie healthCookie = new Cookie("health", "100");
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
