package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		System.out.println("news servelt click");
		
		//request.setAttribute("Ready", "Set_Ready");
		boolean ready = request.getAttribute("ready") == null ? false : (boolean) request.getAttribute("ready");
		
		//if(request.getAttribute("ready") == "true") request.setAttribute("ready", true);
		
		if (!ready) {	
			System.out.println("Ready btn click");
			request.setAttribute("ready", true);
			request.setAttribute("ReadyMessage", "YOU ARE NOT READY");
			request.getRequestDispatcher("News.jsp").forward(request, response);
			//ready = true;
			}
			else {
				request.setAttribute("ready", false);
				request.setAttribute("ReadyMessage", "YOU ARE READY");
				request.getRequestDispatcher("News.jsp").forward(request, response);
				ready = false;
			}
		
		String param = request.getParameter("button");
		
		if (param != null) {

			if (param.equals("logout")) {

				// request.setAttribute("button", param);
				request.getRequestDispatcher("index.jsp").forward(request, response);
			}		
			
			if (param.equals("play")) {
				request.getRequestDispatcher("fight.jsp").forward(request, response);
			}

		} else {
			request.getRequestDispatcher("News.jsp").forward(request, response);
		}

		// }
		// doPost(request, response);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		request.getRequestDispatcher("News.jsp").forward(request, response);

//		System.out.println("news servelt click");
//		
//		String param = request.getParameter("button");
//		if (param.equals("ready")) {
//			
//			System.out.println("Ready btn click");
//			request.setAttribute("ReadyMessage", "You are ready");
//			request.getRequestDispatcher("fight.jsp").forward(request, response);
//		}
//		else if (param.equals("logout")) {
//
//			request.setAttribute("button", param);
//			request.getRequestDispatcher("index.jsp").forward(request, response);
//		} else {
//			request.getRequestDispatcher("News.jsp").forward(request, response);
//		}
	}

	// TODO Auto-generated method stub
	// doGet(request, response);

}
