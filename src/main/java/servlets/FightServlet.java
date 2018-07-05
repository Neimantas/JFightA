package servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.IFightEngine;
import services.impl.FightEngineImpl;
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
    
	private String _playerAName;
	private String _playerBName;
	private String _fightId;
	private IFightEngine _engine;
	private int _round = 0;
	private int _health = 100; //hardcoded
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FightServlet() {
        super();
        _engine = new FightEngineImpl();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		request.getRequestDispatcher("fight.jsp").forward(request, response);
		
		_playerAName = request.getParameter("nameA");
	
//		playerBName = request.getParameter("nameB");
		_fightId = request.getParameter("fightId");
		
		doPost(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		doGet(request, response);
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
		
//		response.getWriter()
//		.append("attackHead: " + attackHead)
//		.append("attackBody: " + attackBody)
//		.append("attackArms: " + attackArms)
//		.append("attackLegs: " + attackLegs)
//		.append("defenceHead: " + defenceHead)
//		.append("defenceBody: " + defenceBody)
//		.append("defenceArms: " + defenceArms)
//		.append("defenceLegs: " + defenceLegs);
		//instead of calling getOpponentData, use _engine.engine n
//		ObjectDTO<FightDataDAL> dto = _engine.getOpponentData(Integer.parseInt(_fightId), _playerAName);
		
		
		
		
		ListDTO<FightDataDAL> dto = _engine.engine(Integer.parseInt(_fightId), _round, _health, _playerAName, action);
		
		if(!dto.success) {
			response.getWriter().append("Error occurred. " + dto.message);
		} else {
			//first sent round param 0, then get heatl results from figth table
			//engine - returns healhtA and healthB
			//
			_round++;
			List<FightDataDAL> dals = dto.transferDataList; //0-you, 1-opponent
			
			
			int playerAHealth = dals.get(0).healthPoints;
			_health = playerAHealth;
			_playerBName = dals.get(1).userId + "";
			int _playerBHealth = dals.get(1).healthPoints;
			
			request.setAttribute("playerAName", _playerAName);
			request.setAttribute("playerBName", _playerBName);
			request.setAttribute("healthA", playerAHealth);
			request.setAttribute("healthB", _playerBHealth);
			//avatar id
			request.setAttribute("id", 1);
			
			
			
			request.getRequestDispatcher("fight.jsp").forward(request, response);
//			request.getRequestDispatcher("NewFile.jsp").forward(request, response);
		}
		
		
		
		
	}

}
