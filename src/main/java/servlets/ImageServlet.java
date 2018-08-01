package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configuration.StartupContainer;
import models.business.ProfileImage;
import models.dto.ObjectDTO;
import services.IImage;
import services.impl.ImageImpl;

@WebServlet(urlPatterns = "/imageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IImage image;

	public ImageServlet() {
		image = StartupContainer.easyDI.getInstance(ImageImpl.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		String user = request.getParameter("user");

		if (id == null) {
			id = "0";
		}

		ObjectDTO<ProfileImage> objectDTO;
		if (user != null && user.equalsIgnoreCase("b")) {
			objectDTO = image.getUserBImage(Integer.parseInt(id));
		} else {
			objectDTO = image.getUserAImage(Integer.parseInt(id));
		}

		if (objectDTO.success && objectDTO.transferData != null) {

			String imageFormat = objectDTO.transferData.imageType != null
					? objectDTO.transferData.imageType.getImageExtension().substring(1)
					: "";

			response.setContentType("image/" + imageFormat);

			ServletOutputStream servletOutputStream = response.getOutputStream();
			servletOutputStream.write(objectDTO.transferData.image);
			response.getOutputStream().close();

		} else {
			response.getWriter().println(objectDTO.message);
			response.getWriter().close();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
