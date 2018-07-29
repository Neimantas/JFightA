package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import configuration.StartupContainer;
import models.dal.ItemDAL;
import models.dto.ObjectDTO;
import services.IItem;
import services.impl.ItemImpl;

@WebServlet("/itemImageServlet")
public class ItemImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private IItem _item;

	public ItemImageServlet() {
		_item = StartupContainer.easyDI.getInstance(ItemImpl.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ObjectDTO<ItemDAL> objectDTO = _item.getItem(Integer.parseInt(request.getParameter("itemId")));
		if (objectDTO.success && objectDTO.transferData != null) {

			String imageFormat = objectDTO.transferData.itemName
					.substring(objectDTO.transferData.itemName.lastIndexOf(".") + 1);

			response.setContentType("/image" + imageFormat);

			ServletOutputStream servletOutputStream = response.getOutputStream();

			servletOutputStream.write(objectDTO.transferData.itemImage);
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
