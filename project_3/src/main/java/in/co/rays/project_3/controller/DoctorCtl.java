
package in.co.rays.project_3.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.project_3.dto.BaseDTO;
import in.co.rays.project_3.dto.QualityDTO;
import in.co.rays.project_3.dto.DoctorDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.model.ModelFactory;
import in.co.rays.project_3.model.ProductModelInt;
import in.co.rays.project_3.model.QualityModelInt;
import in.co.rays.project_3.model.DoctorModelInt;
import in.co.rays.project_3.util.DataUtility;
import in.co.rays.project_3.util.DataValidator;
import in.co.rays.project_3.util.PropertyReader;
import in.co.rays.project_3.util.ServletUtility;

@WebServlet(name = "DoctorCtl", urlPatterns = { "/ctl/DoctorCtl" })
public class DoctorCtl extends BaseCtl {

	protected void preload(HttpServletRequest request) {
		HashMap map = new HashMap();
		map.put("Dermatology", "Dermatology");
		map.put("Neurology", "Neurology");
		map.put("Pathology", "Peurology");
		map.put("Psychology", "Psychology");

		request.setAttribute("expertisep", map);

	}

	protected boolean validate(HttpServletRequest request) {
		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "name"));
			pass = false;
			
		}

		if (DataValidator.isNull(request.getParameter("mobile"))) {
			request.setAttribute("mobile", PropertyReader.getValue("error.require", "mobile"));
			pass = false;
		} else if (!DataValidator.isMobileNo(request.getParameter("mobile"))) {
			request.setAttribute("mobile", "Mobile No. Series start with 6-9 ");
			if (!DataValidator.isPhoneLength(request.getParameter("mobile"))) {
				request.setAttribute("mobile", "Mobile No.must be 10 digit ");
			}
			pass = false;
		}
		
	else if (!DataValidator.isPhoneLength(request.getParameter("mobile"))) {
		request.setAttribute("mobile", "Mobile No.must be 10 digit ");
		
		
		pass = false;
	}
		//and No. Series start with 6-9

		if (!OP_UPDATE.equalsIgnoreCase(request.getParameter("operation"))) {

			if (DataValidator.isNull(request.getParameter("expertise"))) {
				request.setAttribute("expertise", PropertyReader.getValue("error.require", "expertise"));
				pass = false;
			}

			if (DataValidator.isNull(request.getParameter("dob"))) {
				request.setAttribute("dob", PropertyReader.getValue("error.require", "dob"));
				pass = false;

			}

		}
		return pass;
	}

	protected BaseDTO populateDTO(HttpServletRequest request) {
		DoctorDTO dto = new DoctorDTO();

		System.out.println(request.getParameter("date"));

		dto.setId(DataUtility.getLong(request.getParameter("id")));
		dto.setName(DataUtility.getString(request.getParameter("name")));
		dto.setMobile(DataUtility.getLong(request.getParameter("mobile")));
		dto.setDob(DataUtility.getDate(request.getParameter("dob")));
		dto.setExpertise(DataUtility.getString(request.getParameter("expertise")));

		populateBean(dto, request);

		return dto;

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String op = DataUtility.getString(request.getParameter("operation"));
		DoctorModelInt model = ModelFactory.getInstance().getDoctorModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (id > 0 || op != null) {
			DoctorDTO dto;
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
		DoctorModelInt model = ModelFactory.getInstance().getDoctorModel();
		long id = DataUtility.getLong(request.getParameter("id"));
		if (OP_SAVE.equalsIgnoreCase(op) || OP_UPDATE.equalsIgnoreCase(op)) {
			DoctorDTO dto = (DoctorDTO) populateDTO(request);
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

			DoctorDTO dto = (DoctorDTO) populateDTO(request);
			try {
				model.delete(dto);
				ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, request, response);
				return;
			} catch (ApplicationException e) {
				ServletUtility.handleException(e, request, response);
				return;
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.DOCTOR_CTL, request, response);
			return;
		}
		ServletUtility.forward(getView(), request, response);

	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.DOCTOR_VIEW;
	}

}
