package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeOtherOrganizationDao;
import ru.kolaer.server.webportal.mvc.model.entities.birthday.EmployeeOtherOrganizationDecorator;

import java.util.*;

@Repository(value = "employeeOtherOrganizationDao")
public class EmployeeOtherOrganizationDaoImpl implements EmployeeOtherOrganizationDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	@Transactional(readOnly = true)
	public List<EmployeeOtherOrganization> getAll() {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("from EmployeeOtherOrganizationDecorator").list();
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EmployeeOtherOrganization> getAllMaxCount(final int count) {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("from EmployeeOtherOrganizationDecorator").setMaxResults(count).list();
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public int getRowCount() {
		final Long result = (Long)  sessionFactory.getCurrentSession().createQuery("SELECT count(x.pnumber) FROM EmployeeOtherOrganizationDecorator x").uniqueResult();
		return result.intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeOtherOrganization> getUserRangeBirthday(final Date startDate, final Date endDate) {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM EmployeeOtherOrganizationDecorator t where t.birthday BETWEEN :startDate AND :endDate")
	            .setParameter("startDate", startDate)
	            .setParameter("endDate", endDate)
	            .list();
		return result;
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<EmployeeOtherOrganization> getUsersByBirthday(final Date date) {

		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM EmployeeOtherOrganizationDecorator t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
	            .setParameter("date", date)
	            .list();
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeOtherOrganization> getUserBirthdayToday() {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("FROM EmployeeOtherOrganizationDecorator t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE)")
	            .list();
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public int getCountUserBirthday(final Date date) {
		final Long result = (Long) sessionFactory.getCurrentSession().createQuery("SELECT count(t.pnumber) FROM EmployeeOtherOrganizationDecorator t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
				.setParameter("date", date)
				.uniqueResult();
		return result.intValue();
	}

	@Override
	@Transactional
	public void insertData(final EmployeeOtherOrganization data) {
		sessionFactory.getCurrentSession().persist(data);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	@Override
	@Transactional
	public void insertDataList(final List<EmployeeOtherOrganization> dataList) {
        for (Iterator<EmployeeOtherOrganization> it = dataList.iterator(); it.hasNext();) {
        	EmployeeOtherOrganization enquiry = it.next();
			sessionFactory.getCurrentSession().persist(enquiry);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
        }	
	}

	@Override
	@Transactional
	public void update(List<EmployeeOtherOrganization> entities) {
		final List<EmployeeOtherOrganization> dbList = sessionFactory.getCurrentSession().createQuery("FROM EmployeeOtherOrganizationDecorator").list();
		Map<String, EmployeeOtherOrganization> mapEmp = new HashMap<>();
		dbList.forEach(emp -> mapEmp.put(emp.getInitials() + emp.getOrganization() + emp.getPost(), emp));

		int i =0;
		for(EmployeeOtherOrganization entity : entities) {
			final String key = entity.getInitials() + entity.getOrganization() + entity.getPost();
			EmployeeOtherOrganization dbEmp = mapEmp.get(key);
			if(dbEmp == null) {
				dbEmp = new EmployeeOtherOrganizationDecorator(entity);
				this.sessionFactory.getCurrentSession().persist(dbEmp);
			} else {
				dbEmp.setBirthday(entity.getBirthday());
				dbEmp.setPost(entity.getPost());
				dbEmp.setDepartament(entity.getDepartament());
				dbEmp.setCategoryUnit(entity.getCategoryUnit());
				dbEmp.setEmail(entity.getEmail());
				dbEmp.setMobilePhone(entity.getMobilePhone());
				dbEmp.setPhone(entity.getPhone());
				dbEmp.setInitials(entity.getInitials());
				dbEmp.setOrganization(entity.getOrganization());
				dbEmp.setvCard(entity.getvCard());
				mapEmp.remove(key);
				this.sessionFactory.getCurrentSession().update(dbEmp);
			}

			i++;
			if(i == 50)
				this.sessionFactory.getCurrentSession().flush();
		}

		mapEmp.values().forEach(this.sessionFactory.getCurrentSession()::delete);

		this.sessionFactory.getCurrentSession().flush();
		this.sessionFactory.getCurrentSession().clear();
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeOtherOrganization> getUsersByBirthdayAndOrg(Date date, String organization) {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM EmployeeOtherOrganizationDecorator t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
				.setParameter("org", organization)
				.setParameter("date", date)
	            .list();
		return result;
	}

	@Override
	@Transactional(readOnly = true)
	public int getCountUserBirthdayAndOrg(Date date, String organization) {
		final Long result = (Long) sessionFactory.getCurrentSession().createQuery("SELECT count(t.pnumber) FROM EmployeeOtherOrganizationDecorator t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
				.setParameter("org", organization)
				.setParameter("date", date)
				.uniqueResult();
		return result.intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public List<EmployeeOtherOrganization> getUsersByInitials(String initials) {
		if(initials == null || initials.isEmpty())
			return Collections.emptyList();
			
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("FROM EmployeeOtherOrganizationDecorator t where t.initials like :initials")
				.setParameter("initials", "%" + initials + "%")
				.list();
		return result;
	}

}
