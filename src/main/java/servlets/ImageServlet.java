package servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet(urlPatterns = "/imageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ImageServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		String id = request.getParameter("id");
        if(id!=null && "1".equalsIgnoreCase(id)){
             response.setContentType("image/jpeg");
//             response.getOutputStream().write( Files.readAllBytes(new File(getServletContext().getRealPath("test.jpg")).toPath()));
//																	"src\\main\\webapp\\resources\\images\\characters\\fighter.jpg" doesn't work? 
             response.getOutputStream().write( Files.readAllBytes(new File("C:\\Users\\Marek\\Desktop\\JFight\\JFightA\\fighter.jpg").toPath()));
             response.getOutputStream().close();
        }else {
             response.getWriter().println("Sample text");
             response.getWriter().close();
        }
        System.out.println(new File("C:\\Users\\Marek\\Desktop\\JFight\\JFightA\\test.jpg").toPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
