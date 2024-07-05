
package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.PrescriptionDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.PrescriptionModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "PrescriptionCtl", urlPatterns = { "/ctl/PrescriptionCtl" })
public class PrescriptionCtl extends BaseCtl {

	protected void preload(HttpServletRequest request) {
		HashMap map = new HashMap();
		map.put("fungal", "fungal");
		map.put("viral", "viral");
		map.put("maleria", "maleria");

		request.setAttribute("deceasep", map);

	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "name"));
			pass = false;
		} else if (!DataValidator.isNamemaxlegntten(request.getParameter("name"))) {
			request.setAttribute("name", "name should be 20 characters");
			pass = false;

		
		}

		if (DataValidator.isNull(request.getParameter("capacity"))) {
			request.setAttribute("capacity", PropertyReader.getValue("error.require", "capacity"));
			System.out.println(pass);
			pass = false;
			/*
			 * } else if (!DataValidator.isInteger(request.getParameter("capacity"))) {
			 * request.setAttribute("capacity", "capacity only integer"); pass = false;
			 * 
			 */

		}
		if (!OP_UPDATE.equalsIgnoreCase(request.getParameter("operation"))) {

			if (DataValidator.isNull(request.getParameter("decease"))) {
				request.setAttribute("decease", PropertyReader.getValue("error.require", "decease"));
				pass = false;
			}

			if (DataValidator.isNull(request.getParameter("date"))) {
				request.setAttribute("date", PropertyReader.getValue("error.require", "date"));
				pass = false;

			}

		}
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		PrescriptionDTO dto = new PrescriptionDTO();

		System.out.println(request.getParameter("date"));

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setDecease(DataUtility.getString(request.getParameter("decease")));
		dto.setDate(DataUtility.getDate(request.getParameter("date")));
		dto.setCapacity(DataUtility.getString(request.getParameter("capacity")));

		populateBean(dto, request);

		return dto;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		PrescriptionModelInt model = ModelFactory.getInstance().getPrescriptionModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			PrescriptionDTO dto;
			try {
				dto = model.findByPK(id);
				ServletUtility.setDto(dto, request);
			} catch (Exception e) {
				e.printStackTrace();
				ServletUtility.handleException(e, request, response);
				return;
			}
		}
		ServletUtility.forward(getView(), request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		PrescriptionModelInt model = ModelFactory.getInstance().getPrescriptionModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			PrescriptionDTO dto = (PrescriptionDTO) populateDTO(request);
			try {
				if (id > 0) {
					model.update(dto);

					ServletUtility.setSuccessMessage("Data is successfully Update", request);
				} else {

					try {
						model.add(dto);

						ServletUtility.setDto(dto, request);
						ServletUtility.setSuccessMessage("Data is successfully saved", request);
					} catch (ApplicationException e) {
						ServletUtility.handleException(e, request, response);
						return;
					} catch (DuplicateRecordException e) {
						ServletUtility.setDto(dto, request);
						ServletUtility.setErrorMessage("Login id already exists", request);
					}

				}
				ServletUtility.setDto(dto, request);

			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			} catch (DuplicateRecordException e) {
				ServletUtility.setDto(dto, request);
				ServletUtility.setErrorMessage("Login id already exists", request);
			}
		} else if (OP_DELETE.equalsIgnoreCase(op)) {

			PrescriptionDTO dto = (PrescriptionDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.PRESCRIPTION_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.PRESCRIPTION_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.PRESCRIPTION_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.PRESCRIPTION_VIEW;
	}

}
