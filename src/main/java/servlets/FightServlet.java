package servlets;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class FightServlet
 */
@WebServlet(urlPatterns = "/fight")
public class FightServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FightServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
//		request.getRequestDispatcher("fight.jsp").forward(request, response);
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
		
		request.setAttribute("playerAName", "Jonas");
		request.setAttribute("playerBName", "Petras");
		request.setAttribute("healthA", 100);
		request.setAttribute("healthB", 100);
		
		request.getRequestDispatcher("fight.jsp").forward(request, response);
		
		
	}

}
