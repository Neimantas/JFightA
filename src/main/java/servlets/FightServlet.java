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
import models.business.FightData;
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
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FightServlet() {
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
		
		if(!_logService.userValidator(request, response)) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		IFightEngine _engine = StartupContainer.easyDI.getInstance(FightEngineImpl.class);
		FightData fightData = new FightData();
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		setFightData(fightData, request);
		if(!fightData.isDataSet()) {
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		Actions action = getActionFromRequest(request);		
		ListDTO<FightDataDAL> dto = _engine.engine(fightData, action); //Sending info to fight engine.
		
		if(!dto.success && dto.message.equals(Error.OPPONENT_IS_MISSING.getMessage())) {
			//when DB didn't find opponent data, it means that player closed his browser. Then auto win
			//Other player did't found, so his id will be -1.
			endFight(fightData.fightId, fightData.playerAUserId, Settings.MISSING_PLAYER_USER_ID, true, false);
			removeCookies(request, response);
			request.getRequestDispatcher("win.jsp").forward(request, response);
		} else {
			
			List<FightDataDAL> dals = dto.transferDataList; //index:0-you, index:1-opponent
			setDataToFront(fightData, dals, request);
			fightData.round++; //increase round;
			response.addCookie(new Cookie("round", Integer.toString(fightData.round))); //add round cookie
			response.addCookie(new Cookie("health", Integer.toString(fightData.playerAHealth))); //add health cookie
			
			if(fightData.playerBHealth<=0 && fightData.playerAHealth<=0) {									//check if fight is lost/win/draw, and react acordingly.
				endFight(fightData.fightId, fightData.playerAUserId, fightData.playerBUserId, false, true);
				removeCookies(request, response);
				request.getRequestDispatcher("draw.jsp").forward(request, response);
			} else if(fightData.playerBHealth<=0) {
				endFight(fightData.fightId, fightData.playerAUserId, fightData.playerBUserId, true, false);
				removeCookies(request, response);
				request.getRequestDispatcher("win.jsp").forward(request, response);
			} else if(fightData.playerAHealth<=0) {
				endFight(fightData.fightId, fightData.playerAUserId, fightData.playerBUserId, false, false);
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
	
	private void setFightData(FightData data, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		for(Cookie c : cookies) {
			if (c.getName().equals("userId"))
				data.setPlayerAUserIdString(c.getValue());
			if (c.getName().equals("fightId"))
				data.setFightIdString(c.getValue());
			if(c.getName().equals("round"))
				data.setRoundString(c.getValue());
			if(c.getName().equals("health"))
				data.setPlayerAHealthString(c.getValue());
		} 
	}
	
	private void setDataToFront(FightData fightData, List<FightDataDAL> dals, HttpServletRequest request) {
		int playerAUserId = fightData.playerAUserId;
		int playerBUserId = dals.get(1).userId;
		fightData.playerBUserId = dals.get(1).userId; //setting opponent userId
		fightData.playerAHealth = dals.get(0).healthPoints;
		fightData.playerBHealth = dals.get(1).healthPoints;
		
		request.setAttribute("playerAName", _cache.getPlayer(playerAUserId).user.name);								//Sending refreshed info to page.
		request.setAttribute("playerBName", _cache.getPlayer(playerBUserId).user.name);
		request.setAttribute("healthA", fightData.playerAHealth);
		request.setAttribute("healthB", fightData.playerBHealth);
		//avatar id
		request.setAttribute("idA", playerAUserId);													
		request.setAttribute("idB", playerBUserId);
		request.setAttribute("attackItemA", _cache.getPlayer(playerAUserId).characterInfo.attackItemId);
		request.setAttribute("defenceItemA", _cache.getPlayer(playerAUserId).characterInfo.defenceItemId);
		request.setAttribute("attackItemB", _cache.getPlayer(playerBUserId).characterInfo.attackItemId);
		request.setAttribute("defenceItemB", _cache.getPlayer(playerBUserId).characterInfo.defenceItemId);
	}
}
