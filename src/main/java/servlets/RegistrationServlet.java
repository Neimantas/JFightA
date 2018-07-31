package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configuration.StartupContainer;
import models.business.UserLoginData;
import models.business.UserRegIn;
import models.dto.DTO;
import models.dto.UserLoginDataDTO;
import services.ILoginService;
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
	private ILoginService _logService;
	
    public RegistrationServlet() {
    	 _logService = StartupContainer.easyDI.getInstance(LoginService.class);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UserRegIn userRegInfo = new UserRegIn();
		userRegInfo.name= request.getParameter("username");
		userRegInfo.password= request.getParameter("pass");
		userRegInfo.mail=request.getParameter("mail");
		UserLoginDataDTO dto=_logService.registration(userRegInfo);
		System.out.println(">>>>>>>>>>>>>>"+dto.userloginData.name);
		System.out.println(">>>>>>>>>>>>>>"+dto.userloginData.password);
		if(dto.success) {
			UserLoginData userLogin=new UserLoginData();
			userLogin.name=dto.userloginData.name;
			userLogin.password=userRegInfo.password;
			DTO playerDTO=_logService.login(response,userLogin);
			if (playerDTO.success) {
				request.getRequestDispatcher("News.jsp").forward(request, response);
			}
			if(!playerDTO.success) {
				request.getRequestDispatcher("index.jsp").forward(request, response);	
			}
		};
		
	}

}