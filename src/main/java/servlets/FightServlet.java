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
import services.ICRUD;
import services.ICache;
import services.IFightEngine;
import services.IHigherService;
import services.ILogger;
import services.ILoginService;
import services.impl.CRUDImpl;
import services.impl.CacheImpl;
import services.impl.FightEngineImpl;
import services.impl.HigherService;
import services.impl.ImageImpl;
import services.impl.LoggerImpl;
import services.impl.LoginService;
import models.dto.ListDTO;
import models.dto.ObjectDTO;
import models.business.Actions;
import models.constant.Error;
import models.constant.Settings;
import models.constant.UserStatus;
import models.dal.FightDataDAL;
import models.dal.ResultDAL;

/**
 * Servlet implementation class FightServlet
 */
@WebServlet(urlPatterns = "/fight")
public class FightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private IHigherService _hService; //should test if it work with multiple users
	private ILogger _logger;
	private ICache _cache;
	private ILoginService _logService;
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
    	_hService = StartupContainer.easyDI.getInstance(HigherService.class);
    	_logger =  StartupContainer.easyDI.getInstance(LoggerImpl.class);
    	_cache = CacheImpl.getInstance();
    	_logService = StartupContainer.easyDI.getInstance(LoginService.class);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
		
		if(!_logService.userValidator(request, response)) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		
		IFightEngine _engine = StartupContainer.easyDI.getInstance(FightEngineImpl.class);
		String playerAUserIdString = "";
		String fightIdString = "";
		String roundString = "";
		String playerAHealthString = "";
//		ICache cache = CacheImpl.getInstance();
		//need to add validation method
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		
		for(Cookie c : cookies) {
			if (c.getName().equals("userId"))
				playerAUserIdString = c.getValue();
			if (c.getName().equals("fightId"))
				fightIdString = c.getValue();
			if(c.getName().equals("round"))
				roundString = c.getValue();
			if(c.getName().equals("health"))
				playerAHealthString = c.getValue();
		} 
		if(playerAUserIdString.equals("") || fightIdString.equals("") || roundString.equals("") || playerAHealthString.equals("")) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
			
		Actions action = getActionFromRequest(request);		
		int round = Integer.parseInt(roundString);
		int playerAHealth = Integer.parseInt(playerAHealthString);
		int fightId = Integer.parseInt(fightIdString);
		int playerAUserId = Integer.parseInt(playerAUserIdString);
		
		ListDTO<FightDataDAL> dto = _engine.engine(fightId, round, playerAHealth, playerAUserId, action); //Sending info to fight engine.
		
		if(!dto.success && dto.message.equals(Error.OPPONENT_IS_MISSING.getMessage())) {
//			response.getWriter().append("Error occurred. " + dto.message); //Print error to page.
			//when DB didn't find opponent data, it means that player closed his browser. Then auto win
//			_cache.getPlayer(playerAUserId).userStatus = UserStatus.NOT_READY;
			//Other player did't found, so his id will be -1.
//			_hService.writeFightResult(fightId, playerAUserId, -1, false);
			endFight(fightId, playerAUserId, Settings.MISSING_PLAYER_USER_ID, true, false);
			removeCookies(request, response);
			request.getRequestDispatcher("win.jsp").forward(request, response);
		} else {
			//first sent round param 0, then get heatl results from figth table
			//engine - returns healhtA and healthB
			//
			round++; //after success increment round counter
			List<FightDataDAL> dals = dto.transferDataList; //index:0-you, index:1-opponent
			playerAHealth = dals.get(0).healthPoints;
			int playerBUserId = dals.get(1).userId;
			int playerBHealth = dals.get(1).healthPoints;
			String playerAName = _cache.getPlayer(playerAUserId).user.name;
			String playerBName = _cache.getPlayer(playerBUserId).user.name;
			
			int attackItemAId = _cache.getPlayer(playerAUserId).characterInfo.attackItemId;
			int defenceItemAId = _cache.getPlayer(playerAUserId).characterInfo.defenceItemId;
			int attackItemBId = _cache.getPlayer(playerBUserId).characterInfo.attackItemId;
			int defenceItemBId = _cache.getPlayer(playerBUserId).characterInfo.defenceItemId;
			
			request.setAttribute("playerAName", playerAName);								//Sending refreshed info to page.
			request.setAttribute("playerBName", playerBName);
			request.setAttribute("healthA", playerAHealth);
			request.setAttribute("healthB", playerBHealth);
			//avatar id
			request.setAttribute("idA", playerAUserId);													
			request.setAttribute("idB", playerBUserId);
			request.setAttribute("attackItemA", attackItemAId);
			request.setAttribute("defenceItemA", defenceItemAId);
			request.setAttribute("attackItemB", attackItemBId);
			request.setAttribute("defenceItemB", defenceItemBId);
			
			response.addCookie(new Cookie("round", Integer.toString(round))); //add round cookie
			response.addCookie(new Cookie("health", Integer.toString(playerAHealth))); //add health cookie
			
			if(playerBHealth<=0 && playerAHealth <= 0) {									//check if fight is lost/win/draw, and react acordingly.
				//to separate method
//				_cache.getPlayer(playerAUserId).userStatus = UserStatus.NOT_READY;
//				_hService.writeFightResult(fightId, playerAUserId, playerBUserId, true);
				endFight(fightId, playerAUserId, playerBUserId, false, true);
				removeCookies(request, response);
				request.getRequestDispatcher("draw.jsp").forward(request, response);
			} else if(playerBHealth<=0) {
//				_cache.getPlayer(playerAUserId).userStatus = UserStatus.NOT_READY;
//				_hService.writeFightResult(fightId, playerAUserId, playerBUserId, false);
//				_logger.logFightData(fightId, playerAUserId, playerBUserId);
				endFight(fightId, playerAUserId, playerBUserId, true, false);
				removeCookies(request, response);
				request.getRequestDispatcher("win.jsp").forward(request, response);
			} else if(playerAHealth<=0) {
//				_cache.getPlayer(playerAUserId).userStatus = UserStatus.NOT_READY;
				endFight(fightId, playerAUserId, playerBUserId, false, false);
				removeCookies(request, response);
				request.getRequestDispatcher("lost.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("fight.jsp").forward(request, response);			//if fight is not finised - refresh page with new data.
			}
		}
	}
	
	//fight cookies remover
	private void removeCookies(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		for(Cookie c : cookies) {
			if (c.getName().equals("userId")) {
				c.setMaxAge(0);
				response.addCookie(c);
				System.out.println("userId is removed");
			}	
			if (c.getName().equals("fightId")) {
				c.setMaxAge(0);
				response.addCookie(c);
			}
			if(c.getName().equals("round")) {
				c.setMaxAge(0);
				response.addCookie(c);
			}
			if(c.getName().equals("health")) {
				c.setMaxAge(0);
				response.addCookie(c);
			}
		} 
	}
	
	private void endFight(int fightId, int userAId, int userBId, boolean win, boolean draw) {
		if(win) {
			_cache.getPlayer(userAId).userStatus = UserStatus.NOT_READY;
			_hService.writeFightResult(fightId, userAId, userBId, false);
			_logger.logFightData(fightId, userAId, userBId);
		} else if(draw) {
			_cache.getPlayer(userAId).userStatus = UserStatus.NOT_READY;
			_hService.writeFightResult(fightId, userAId, userBId, true);
		} else {
			_cache.getPlayer(userAId).userStatus = UserStatus.NOT_READY;
		}
	}
	
	private Actions getActionFromRequest(HttpServletRequest request) {
		Actions action = new Actions();
		action.attackArms = request.getParameter("attackArms") == null?0:1;
		action.attackBody = request.getParameter("attackBody") == null?0:1;
		action.attackHead = request.getParameter("attackHead") == null?0:1;
		action.attackLegs = request.getParameter("attackLegs") == null?0:1;
		action.defenceArms = request.getParameter("defenceArms") == null?0:1;
		action.defenceBody = request.getParameter("defenceBody") == null?0:1;
		action.defenceHead = request.getParameter("defenceHead") == null?0:1;
		action.defenceLegs = request.getParameter("defenceLegs") == null?0:1;
		return action;
	}
}
