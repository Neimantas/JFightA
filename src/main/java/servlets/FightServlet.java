package servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.IFightEngine;
import services.impl.FightEngineImpl;
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
		System.out.println("NameA " + _playerAName + " _fightId " + _fightId);
		
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
		
//		response.getWriter()
//		.append("attackHead: " + attackHead)
//		.append("attackBody: " + attackBody)
//		.append("attackArms: " + attackArms)
//		.append("attackLegs: " + attackLegs)
//		.append("defenceHead: " + defenceHead)
//		.append("defenceBody: " + defenceBody)
//		.append("defenceArms: " + defenceArms)
//		.append("defenceLegs: " + defenceLegs);
		
		ObjectDTO<FightDataDAL> dto = _engine.getOpponentName(Integer.parseInt(_fightId), _playerAName);
		if(!dto.success) {
			response.getWriter().append("Error occurred. " + dto.message);
		} else {
			FightDataDAL dal = dto.transferData;
			_playerBName = dal.userId + "";
			request.setAttribute("playerAName", _playerAName);
			request.setAttribute("playerBName", _playerBName);
			request.setAttribute("healthA", 100);
			request.setAttribute("healthB", 100);
			request.setAttribute("id", 1);
			
			
			
			request.getRequestDispatcher("fight.jsp").forward(request, response);
//			request.getRequestDispatcher("NewFile.jsp").forward(request, response);
		}
		
		
		
		
	}

}
