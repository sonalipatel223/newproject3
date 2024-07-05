
package in.co.rays.project_3.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.JobDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class JobModelHibImp implements JobModelInt{

	@Override
	public long add(JobDTO dto) throws ApplicationException, DuplicateRecordException {
		
		 JobDTO existDto = null;
			
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
				throw new ApplicationException("Exception in Job Add " + e.getMessage());
			} finally {
				session.close();
			}


		return dto.getId();
	}

	@Override
	public void delete(JobDTO dto) throws ApplicationException {

		
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
			throw new ApplicationException("Exception in Job Delete" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public void update(JobDTO dto) throws ApplicationException, DuplicateRecordException {
		
		
		Session session = null;
		
		/*
		 * Transaction tx = null; JobDTO exesistDto = findByLogin(dto.getLogin());
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
			throw new ApplicationException("Exception in Job update" + e.getMessage());
		} finally {
			session.close();
		}

	}

	@Override
	public JobDTO findByPK(long pk) throws ApplicationException {
		
		
		Session session = null;
		JobDTO dto = null;
		try {
			session = HibDataSource.getSession();
			dto = (JobDTO) session.get(JobDTO.class, pk);

		} catch (HibernateException e) {
			throw new ApplicationException("Exception : Exception in getting Bank by pk");
		} finally {
			session.close();
		}

		return dto;
	}

	@Override
	public JobDTO findByLogin(String login) throws ApplicationException {
		
		
		
		Session session = null;
		JobDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(JobDTO.class);
			criteria.add(Restrictions.eq("login", login));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (JobDTO) list.get(0);
			}
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in getting Job by Login " + e.getMessage());

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
			Criteria criteria = session.createCriteria(JobDTO.class);
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
	public List search(JobDTO dto, int pageNo, int pageSize) throws ApplicationException {
		
		Session session = null;
		ArrayList<JobDTO> list = null;
		try {
			session = HibDataSource.getSession();
			System.out.println("---------------------------------");
			Criteria criteria = session.createCriteria(JobDTO.class);
			if (dto != null) {
				
				if (dto.getId() != null && dto.getId() > 0) {
					criteria.add(Restrictions.eq("id", dto.getId()));
				}
				if (dto.getTitle() != null && dto.getTitle().length() > 0) {
					criteria.add(Restrictions.like("title", dto.getTitle() + "%"));
				}
				
				  if (dto.getExperience() != null && dto.getExperience().length() > 0) {
				  criteria.add(Restrictions.like("experience", dto.getExperience() + "%"));
				  }
					
				  if (dto.getStatus() != null && dto.getStatus().length() > 0) {
					  criteria.add(Restrictions.like("status", dto.getStatus() + "%"));
					  }
						
					if (dto.getDateOfOpning()!= null && dto.getDateOfOpning().getTime() > 0) {
						criteria.add(Restrictions.eq("dateOfOpning", dto.getDateOfOpning()));
					}
					
					
			}
					
					if (pageSize > 0) {
						pageNo = (pageNo - 1) * pageSize;
						criteria.setFirstResult(pageNo);
						criteria.setMaxResults(pageSize);
					}
					list = (ArrayList<JobDTO>) criteria.list();
				} catch (HibernateException e) {
					throw new ApplicationException("Exception in Job search");
				} finally {
					session.close();
				}

		
		return list;
	}

	@Override
	public List search(JobDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return search(dto,0,0);
	}

	@Override
	public List getRoles(JobDTO dto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List list() throws ApplicationException {
		// TODO Auto-generated method stub
		return list(0,0);
	}

}
