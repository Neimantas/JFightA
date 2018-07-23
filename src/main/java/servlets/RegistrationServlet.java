package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.business.UserLoginData;
import models.business.UserRegIn;
import services.impl.LoginService;

/**
 * Servlet implementation class RegistrationServlet
 */
@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegistrationServlet() {
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
		String mail = request.getParameter("mail");
		UserRegIn userReg = new UserRegIn();
		userReg.name = userName;
		userReg.password = pass;
		userReg.mail = mail;
		LoginService log = new LoginService();
		boolean success=log.registration(userReg)._success;
		if (success) {
			request.getRequestDispatcher("News.jsp").forward(request, response);
		}
		else {
			request.getRequestDispatcher("registration.jsp").forward(request, response);
		}
	}

}