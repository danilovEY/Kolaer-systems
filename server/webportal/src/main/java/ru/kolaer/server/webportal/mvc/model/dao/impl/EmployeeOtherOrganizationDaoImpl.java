package ru.kolaer.server.webportal.mvc.model.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.api.mvp.model.restful.EmployeeOtherOrganization;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeOtherOrganizationDao;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class EmployeeOtherOrganizationDaoImpl implements EmployeeOtherOrganizationDao {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<EmployeeOtherOrganization> getAll() {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("from DbBirthdayAll").list();
		return result;
	}
	
	@Override
	public List<EmployeeOtherOrganization> getAllMaxCount(final int count) {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("from DbBirthdayAll").setMaxResults(count).list();
		return result;
	}

	@Override
	public int getRowCount() {
		final Number result = (Number)  sessionFactory.getCurrentSession().createQuery("SELECT count(x) FROM DbBirthdayAll x").uniqueResult();
		return result.intValue();
	}

	@Override
	public List<EmployeeOtherOrganization> getUserRangeBirthday(final Date startDate, final Date endDate) {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM DbBirthdayAll t where t.birthday BETWEEN :startDate AND :endDate")
	            .setParameter("startDate", startDate)
	            .setParameter("endDate", endDate)
	            .list();
		return result;
	}
	
	@Override
	public List<EmployeeOtherOrganization> getUsersByBirthday(final Date date) {

		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM DbBirthdayAll t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
	            .setParameter("date", date)
	            .list();
		return result;
	}

	@Override
	public List<EmployeeOtherOrganization> getUserBirthdayToday() {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("FROM DbBirthdayAll t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE)")
	            .list();
		return result;
	}

	@Override
	public int getCountUserBirthday(final Date date) {
		final Long result = (Long) sessionFactory.getCurrentSession().createQuery("SELECT count(t) FROM DbBirthdayAll t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
				.setParameter("date", date)
				.uniqueResult();
		return result.intValue();
	}

	@Override
	public void insertData(final EmployeeOtherOrganization data) {
		sessionFactory.getCurrentSession().persist(data);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	@Override
	public void insertDataList(final List<EmployeeOtherOrganization> dataList) {
        List<EmployeeOtherOrganization> tempEnqList = dataList;
        for (Iterator<EmployeeOtherOrganization> it = tempEnqList.iterator(); it.hasNext();) {
        	EmployeeOtherOrganization enquiry = it.next();
			sessionFactory.getCurrentSession().persist(enquiry);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
        }	
	}

	@Override
	public List<EmployeeOtherOrganization> getUsersByBirthdayAndOrg(Date date, String organization) {
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM DbBirthdayAll t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
				.setParameter("org", organization)
				.setParameter("date", date)
	            .list();
		return result;
	}

	@Override
	public int getCountUserBirthdayAndOrg(Date date, String organization) {
		final Number result = (Number) sessionFactory.getCurrentSession().createQuery("SELECT count(t) FROM DbBirthdayAll t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
				.setParameter("org", organization)
				.setParameter("date", date)
				.list();
		return result.intValue();
	}

	@Override
	public List<EmployeeOtherOrganization> getUsersByInitials(String initials) {
		if(initials == null || initials.isEmpty())
			return Collections.emptyList();
			
		final List<EmployeeOtherOrganization> result = sessionFactory.getCurrentSession().createQuery("FROM DbBirthdayAll t where t.initials like :initials")
				.setParameter("initials", "%" + initials + "%")
				.list();
		return result;
	}

}
