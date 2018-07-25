package servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configuration.StartupContainer;
import services.IFightEngine;
import services.impl.FightEngineImpl;
import services.impl.ItemImpl;
import models.dto.ActionsDTO;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import models.dal.FightDataDAL;

/**
 * Servlet implementation class FightServlet
 */
@WebServlet(urlPatterns = "/fight")
public class FightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
//	private String _playerAName;
//	private String _playerBName;
//	private String _fightId;
//	private IFightEngine _engine;
//	private int _round = 0;
//	private int _health = 100; //hardcoded, let's make it not hardcoded :)
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FightServlet() {
//    	_engine = StartupContainer.easyDI.getInstance(FightEngineImpl.class);
//        _engine = new FightEngineImpl();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		request.getRequestDispatcher("fight.jsp").forward(request, response);
		
//		String playerName = request.getParameter("name");
//	
////		playerBName = request.getParameter("nameB");
//		String fightId = request.getParameter("fightId");
//		Setting cookie with user Id
		
		doPost(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		IFightEngine _engine = StartupContainer.easyDI.getInstance(FightEngineImpl.class);
		String userId = "";
		String fightId = "";
		String round = "";
		String health = "";
		
		Cookie[] cookies = request.getCookies();
		if (cookies == null)
			response.sendRedirect("index.jsp");
		
		System.out.println( request.getCookies());
		System.out.println(cookies.length);
		for(Cookie c : cookies) {
			System.out.println(c.getName() + " " + c.getValue());
			if (c.getName().equals("userId"))
				userId = c.getValue();
			if (c.getName().equals("fightId"))
				fightId = c.getValue();
			if(c.getName().equals("round"))
				round = c.getValue();
			if(c.getName().equals("health"))
				health = c.getValue();
		} 
		if(userId == "" || fightId == "" || round == "" || health == "") {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
			
		
		
		String attackHead = request.getParameter("attackHead");
		String attackBody = request.getParameter("attackBody");
		String attackArms = request.getParameter("attackArms");
		String attackLegs = request.getParameter("attackLegs");
		
		String defenceHead = request.getParameter("defenceHead");
		String defenceBody = request.getParameter("defenceBody");
		String defenceArms = request.getParameter("defenceArms");
		String defenceLegs = request.getParameter("defenceLegs");
		
		ActionsDTO action = new ActionsDTO();
		
		action.attackArms = attackArms == null?0:1;
		action.attackBody = attackBody == null?0:1;
		action.attackHead = attackHead == null?0:1;
		action.attackLegs = attackLegs == null?0:1;
		
		action.defenceArms = defenceArms == null?0:1;
		action.defenceBody = defenceBody == null?0:1;
		action.defenceHead = defenceHead == null?0:1;
		action.defenceLegs = defenceLegs == null?0:1;
		
		int roundI = Integer.parseInt(round);
		int healthI = Integer.parseInt(health);
		ListDTO<FightDataDAL> dto = _engine.engine(Integer.parseInt(fightId), roundI, healthI, userId, action); //Sending info to fight engine.
		
		if(!dto.success) {
			response.getWriter().append("Error occurred. " + dto.message); //Print error to page.
		} else {
			//first sent round param 0, then get heatl results from figth table
			//engine - returns healhtA and healthB
			//
			roundI++;
			
			
			List<FightDataDAL> dals = dto.transferDataList; //index:0-you, index:1-opponent
			
			
			int playerAHealth = dals.get(0).healthPoints;
			healthI = playerAHealth;
			String playerBName = dals.get(1).userId + "";
			int _playerBHealth = dals.get(1).healthPoints;
			
			
			
			request.setAttribute("playerAName", userId);								//Sending refreshed info to page.
			request.setAttribute("playerBName", playerBName);
			request.setAttribute("healthA", playerAHealth);
			request.setAttribute("healthB", _playerBHealth);
			//avatar id
			request.setAttribute("idA", 1);													//Picture ID. Still hardcoded.
			request.setAttribute("idB", 2);
			
			response.addCookie(new Cookie("round", Integer.toString(roundI))); //add round cookie
			response.addCookie(new Cookie("health", Integer.toString(healthI))); //add health cookie
			
			if(_playerBHealth<=0 && playerAHealth <= 0) {									//check if fight is lost/win/draw, and react acordingly.
				request.getRequestDispatcher("draw.jsp").forward(request, response);
			} else if(_playerBHealth<=0) {
				request.getRequestDispatcher("win.jsp").forward(request, response);
			} else if(playerAHealth<=0) {
				request.getRequestDispatcher("lost.jsp").forward(request, response);
			}
			
			request.getRequestDispatcher("fight.jsp").forward(request, response);			//if fight is not finised - refresh page with new data.
//			request.getRequestDispatcher("NewFile.jsp").forward(request, response);
		}
		
		
		
		
	}

}
