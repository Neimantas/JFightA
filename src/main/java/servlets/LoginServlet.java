package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.business.User;
import services.impl.LoginService;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// String pass=request.getParameter("pass");
		// String userName=request.getParameter("username");
		// doGet(request, response);
		//
		// System.out.println("User name "+userName+" paswordas:"+pass);
		// LoginService login=new LoginService();
		//
		// if(login.login(userName, pass)==true) {
		//
		// }
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("username");
		String pass = request.getParameter("pass");

		User user = new User();
		user.name = request.getParameter("username");
		user.password = request.getParameter("pass");

		LoginService login = new LoginService();

		// System.out.println("User name "+userName+" paswordas:"+pass);
		// request.setAttribute("userName",userName);
		if (login.login(userName, pass) == true) {
			System.out.println("pateko i true");
			request.getRequestDispatcher("test.jsp").forward(request, response);
		}
		if (login.login(userName, pass) != true) {
			System.out.println("pateko i false");
			request.getRequestDispatcher("index.jsp");

		}

	}

}