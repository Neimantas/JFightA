package servlets;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.business.User;
import models.business.UserLoginData;
import services.impl.HigherLoginService;
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
	LoginService logService;

	public LoginServlet() {
		// super();
		// logService = StartupContainer.easyDI.getInstance(LoginService.class);
	}

	// public LoginServlet(LoginService logServiceImpl) {
	// logService = logServiceImpl;
	// }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * This one takes, user login info from index.jsp form and sends it to login
	 * server that gives response if user User Name and password is valid if they
	 * are valid refers to Users page(now to test.jsp) with user id data
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userName = request.getParameter("username");
		String pass = request.getParameter("pass");

		UserLoginData user = new UserLoginData();
		user.name = userName;
		user.password = pass;
		LoginService log=new LoginService();
		if(log.login(user)._success) {
			System.out.println("atejo atgal");
			request.getRequestDispatcher("test.jsp").forward(request, response);
		}
//		System.out.println(log.login(user)._message);
//		System.out.println(log.login(user)._user.name);
//		System.out.println(log.login(user)._user.userId);
		// logService.arVeikia();
		// logService.login(user);

		// System.out.println("User name "+userName+" paswordas:"+pass);
		// request.setAttribute("userName",userName);
		// if (login.login(userName, pass) == true) {
		// System.out.println("pateko i true");
		// request.getRequestDispatcher("test.jsp").forward(request, response);
		// }
		// if (login.login(userName, pass) != true) {
		// System.out.println("pateko i false");
		// request.getRequestDispatcher("index.jsp");

		// }

	}

}