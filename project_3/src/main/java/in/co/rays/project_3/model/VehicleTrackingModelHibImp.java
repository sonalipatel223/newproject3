
package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.VehicleTrackingDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class VehicleTrackingModelHibImp implements VehicleTrackingModelInt{

	@Override
	public long add(VehicleTrackingDTO dto) throws ApplicationException, DuplicateRecordException {
		
		 VehicleTrackingDTO existDto = null;
			
			Session session = HibDataSource.getSession();
			Transaction tx = null;
			try {

				tx = session.beginTransaction();

				session.save(dto);

				dto.getId();
				tx.commit();
			} catch (HibernateException e) {
				e.printStackTrace();
				if (tx != null) {
					tx.rollback();

				}
				throw new ApplicationException("Exception in VehicleTracking Add " + e.getMessage());
			} finally {
				session.close();
			}


		return dto.getId();
	}

	@Override
	public void delete(VehicleTrackingDTO dto) throws ApplicationException {

		
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in VehicleTracking Delete" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public void update(VehicleTrackingDTO dto) throws ApplicationException, DuplicateRecordException {
		
		
		Session session = null;
		
		/*
		 * Transaction tx = null; VehicleTrackingDTO exesistDto = findByLogin(dto.getLogin());
		 * 
		 * if (exesistDto != null && exesistDto.getId() != dto.getId()) { throw new
		 * DuplicateRecordException("Login id already exist"); }
		 * 
		 */		  Transaction tx = null;
		 

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.saveOrUpdate(dto);
			tx.commit();
		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in VehicleTracking update" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public VehicleTrackingDTO findByPK(long pk) throws ApplicationException {
		
		
		Session session = null;
		VehicleTrackingDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (VehicleTrackingDTO) session.get(VehicleTrackingDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting Bank by pk");
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public VehicleTrackingDTO findByLogin(String login) throws ApplicationException {
		
		
		
		Session session = null;
		VehicleTrackingDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(VehicleTrackingDTO.class);
			criteria.add(Restrictions.eq("login", login));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (VehicleTrackingDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting VehicleTracking by Login " + e.getMessage());

		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(VehicleTrackingDTO.class);
			if (pageSize > 0) {
				pageNo = (pageNo - 1) * pageSize;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in  Banks list");
		} finally {
			session.close();
		}

		return list;
	}

	/*
	 * @Override public List list(int pageNo, int pageSize) throws
	 * ApplicationException { // TODO Auto-generated method stub return null; }
	 */
	@Override
	public List search(VehicleTrackingDTO dto, int pageNo, int pageSize) throws ApplicationException {
		
		Session session = null;
		ArrayList<VehicleTrackingDTO> list = null;
		try {
			session = HibDataSource.getSession();
			System.out.println("---------------------------------");
			Criteria criteria = session.createCriteria(VehicleTrackingDTO.class);
			if (dto != null) {
				
				/*
				 * if (dto.getId() != null && dto.getId() > 0) {
				 * criteria.add(Restrictions.eq("id", dto.getId())); }
				 */
				if ( dto.getLat() > 0) {
					criteria.add(Restrictions.eq("lat", dto.getLat()));
				}
				
				if ( dto.getLongitude() > 0) {
					criteria.add(Restrictions.eq("longitude", dto.getLongitude() ));
				}
				
					
				 if (dto.getVehicleId()>0) {
					  criteria.add(Restrictions.eq("vehicleId", dto.getVehicleId()));
					  }
						
						
				  if (dto.getDate() != null && dto.getDate().getTime() > 0) {
						criteria.add(Restrictions.eq("date", dto.getDate()));
					}
					
			}
					
					if (pageSize > 0) {
						pageNo = (pageNo - 1) * pageSize;
						criteria.setFirstResult(pageNo);
						criteria.setMaxResults(pageSize);
					}
					list = (ArrayList<VehicleTrackingDTO>) criteria.list();
				} catch (HibernateException e) {
					throw new ApplicationException("Exception in VehicleTracking search");
				} finally {
					session.close();
				}

		
		return list;
	}

	@Override
	public List search(VehicleTrackingDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto,0,0);
	}

	@Override
	public List getRoles(VehicleTrackingDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0,0);
	}

}
