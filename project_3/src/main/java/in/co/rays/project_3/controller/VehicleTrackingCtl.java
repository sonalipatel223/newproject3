
package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.VehicleTrackingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.VehicleTrackingModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "VehicleTrackingCtl", urlPatterns = { "/ctl/VehicleTrackingCtl" })
public class VehicleTrackingCtl extends BaseCtl {

	protected void preload(HttpServletRequest request) {
		HashMap map = new HashMap();
		map.put("1", "1");
		map.put("2", "2");
		map.put("3", "3");
		map.put("4", "4");

		request.setAttribute("vehicleIdp", map);
	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("lat"))) {
			request.setAttribute("lat", PropertyReader.getValue("error.require", "lat"));
			pass = false;

		

		}
		if (DataValidator.isNull(request.getParameter("longitude"))) {
			request.setAttribute("longitude", PropertyReader.getValue("error.require", "longitude"));
			pass = false;

		}
			/*
			 * } else if (!DataValidator.isInteger(request.getParameter("version"))) {
			 * request.setAttribute("version", "version must contains integer only");
			 * System.out.println(pass); pass = false;
			 */
		
		if (!OP_UPDATE.equalsIgnoreCase(request.getParameter("operation"))) {

			if (DataValidator.isNull(request.getParameter("vehicleId"))) {
				request.setAttribute("vehicleId", PropertyReader.getValue("error.require", "vehicleId"));
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
		VehicleTrackingDTO dto = new VehicleTrackingDTO();

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setLat(DataUtility.getDouble(request.getParameter("lat")));
		dto.setLongitude(DataUtility.getDouble(request.getParameter("longitude")));

		dto.setVehicleId(DataUtility.getInt(request.getParameter("vehicleId")));

		dto.setDate(DataUtility.getDate(request.getParameter("date")));

		populateBean(dto, request);

		return dto;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		VehicleTrackingModelInt model = ModelFactory.getInstance().getVehicleTrackingModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			VehicleTrackingDTO dto;
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
		VehicleTrackingModelInt model = ModelFactory.getInstance().getVehicleTrackingModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			VehicleTrackingDTO dto = (VehicleTrackingDTO) populateDTO(request);
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

			VehicleTrackingDTO dto = (VehicleTrackingDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.VEHICLETRACKING_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.VEHICLETRACKING_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.VEHICLETRACKING_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.VEHICLETRACKING_VIEW;
	}

}
