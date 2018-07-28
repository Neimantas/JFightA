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
import models.dto.ListDTO;
import services.ICRUD;
import services.impl.CRUDImpl;
import services.impl.FightEngineImpl;
import services.impl.ImageImpl;

@WebServlet("/itemImageServlet")
public class ItemImageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private ICRUD _crud;

	public ItemImageServlet() {
		_crud = StartupContainer.easyDI.getInstance(CRUDImpl.class);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		ItemDAL dal = new ItemDAL();

		dal.itemId = Integer.parseInt(request.getParameter("itemId"));

		ListDTO<ItemDAL> dto = _crud.read(dal);
		if (dto.success && dto.transferDataList != null) {

			String imageFormat = dto.transferDataList.get(0).itemName
					.substring(dto.transferDataList.get(0).itemName.lastIndexOf(".") + 1);

			response.setContentType("image/" + imageFormat);

			ServletOutputStream servletOutputStream = response.getOutputStream();

			servletOutputStream.write(dto.transferDataList.get(0).itemImage);
			response.getOutputStream().close();
		} else {
			response.getWriter().println(dto.message);
			response.getWriter().close();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
