package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configuration.StartupContainer;
import models.dal.ImageDAL;
import models.dto.ObjectDTO;
import services.IItem;
import services.impl.ItemImpl;

@WebServlet(urlPatterns = "/imageServlet")
public class ImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IItem _item;

	public ImageServlet() {
		_item = StartupContainer.easyDI.getInstance(ItemImpl.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		String user = request.getParameter("user");

		if (id == null) {
			id = "0";
		}

		ObjectDTO<ImageDAL> objectDTO;
		if (user.equalsIgnoreCase("a")) {
			objectDTO = _item.getUserAImage(Integer.parseInt(id));
		} else {
			objectDTO = _item.getUserBImage(Integer.parseInt(id));
		}

		if (objectDTO.success && objectDTO.transferData != null) {

			String imageFormat = objectDTO.transferData.imageName
					.substring(objectDTO.transferData.imageName.lastIndexOf(".") + 1);

			response.setContentType("image/" + imageFormat);

			ServletOutputStream servletOutputStream = response.getOutputStream();

			byte[] buffer = new byte[4096];
			int n = 0;
			while (-1 != (n = objectDTO.transferData.image.read(buffer))) {
				servletOutputStream.write(buffer, 0, n);
			}

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
