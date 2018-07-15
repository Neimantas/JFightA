package servlets;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Object;

import models.dal.ImageDAL;
import models.dto.ObjectDTO;
import services.IImage;
import services.impl.ImageImpl;

/**
 * Servlet implementation class ImageServlet
 */
@WebServlet(urlPatterns = "/imageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IImage _image = ImageImpl.getInstance();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ImageServlet() {
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
		String id = request.getParameter("id");

		if (id != null) {

			ObjectDTO<ImageDAL> objectDTO = _image.getImage(Integer.parseInt(id));

			if (objectDTO.success && objectDTO.transferData != null) {

				String imageFormat = objectDTO.transferData.imageName
						.substring(objectDTO.transferData.imageName.lastIndexOf(".") + 1);

				response.setContentType("image/" + imageFormat);

				ServletOutputStream servletOutputStream = response.getOutputStream();

				byte[] buffer = new byte[4096];
				int n = 0;
				while (-1 != (n = objectDTO.transferData.inputStream.read(buffer))) {
					servletOutputStream.write(buffer, 0, n);
				}

			}

			// response.getOutputStream().write( Files.readAllBytes(new
			// File(getServletContext().getRealPath("test.jpg")).toPath()));
			//

			// response.getOutputStream().write( Files.readAllBytes(new
			// File("C:\\00.Projektai\\JFightA\\src\\main\\webapp\\resources\\images\\characters\\default1.jpg").toPath()));
			response.getOutputStream().close();
		} else {
			response.getWriter().println("Sample text");
			response.getWriter().close();
		}
		// System.out.println(new
		// File("C:\\Users\\Marek\\Desktop\\JFight\\JFightA\\test.jpg").toPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
