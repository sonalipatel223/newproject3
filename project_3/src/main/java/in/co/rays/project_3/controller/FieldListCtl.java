
package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.FieldDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.model.FieldModelInt;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "FieldListCtl", urlPatterns = "/ctl/FieldListCtl")
public class FieldListCtl extends BaseCtl {
	
	protected void preload(HttpServletRequest request) {
		HashMap map = new HashMap();
		map.put("it", "it");
		map.put("bank", "bank");
		map.put("CA", "CA");

		request.setAttribute("typep", map);

		}

	

	@Override
	protected BaseDTO populateDTO(HttpServletRequest request) {
		FieldDTO dto = new FieldDTO();

		dto.setLabel(DataUtility.getString(request.getParameter("label")));
		 dto.setDescription(DataUtility.getString(request.getParameter("description")));
        dto.setType(DataUtility.getString(request.getParameter("type")));
        dto.setActive(DataUtility.getString(request.getParameter("active")));
       

		populateBean(dto, request);
		return dto;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List list;
		List next;
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));
		FieldDTO dto = (FieldDTO) populateDTO(request);

		FieldModelInt model = ModelFactory.getInstance().getFieldModel();
		try {
			list = model.search(dto, pageNo, pageSize);

			ArrayList a = (ArrayList<FieldDTO>) list;

			next = model.search(dto, pageNo + 1, pageSize);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found ", request);
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);
		} catch (ApplicationException e) {
			ServletUtility.handleException(e, request, response);
			return;
		} catch (Exception e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List list = null;
		List next = null;
		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;
		FieldDTO dto = (FieldDTO) populateDTO(request);
		String op = DataUtility.getString(request.getParameter("operation"));

		String[] ids = request.getParameterValues("ids");
		FieldModelInt model = ModelFactory.getInstance().getFieldModel();
		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || "Next".equalsIgnoreCase(op) || "Previous".equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					if(request.getParameter("label").equals("") && request.getParameter("description").equals("") && request.getParameter("type").equals("") && request.getParameter("active").equals("")) {
						ServletUtility.setErrorMessage(" Fill at least one search field", request);
					}
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}

			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.FIELD_CTL, request, response);
				return;
			} else if (OP_RESET.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.FIELD_LIST_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					FieldDTO deletedto = new FieldDTO();
					for (String id : ids) {
						deletedto.setId(DataUtility.getLong(id));
						model.delete(deletedto);
						ServletUtility.setSuccessMessage("Data Deleted Successfully", request);
					}
				} else {
					ServletUtility.setErrorMessage("Select at least one record", request);
				}
			}
			if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.FIELD_LIST_CTL, request, response);
				return;
			}
			dto = (FieldDTO) populateDTO(request);
			list = model.search(dto, pageNo, pageSize);
		
			ServletUtility.setDto(dto, request);
			next = model.search(dto, pageNo + 1, pageSize);

			ServletUtility.setList(list, request);
			ServletUtility.setList(list, request);
			if (list == null || list.size() == 0) {
				if (!OP_DELETE.equalsIgnoreCase(op)) {
					ServletUtility.setErrorMessage("No record found ", request);
				}
			}
			if (next == null || next.size() == 0) {
				request.setAttribute("nextListSize", 0);

			} else {
				request.setAttribute("nextListSize", next.size());
			}
			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.forward(getView(), request, response);

		} catch (ApplicationException e) {
			ServletUtility.handleException(e, request, response);
			return;
		} catch (Exception e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected String getView() {
		return ORSView.FIELD_LIST_VIEW;
	}
}
