package ru.kolaer.server.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.dao.DbBirthdayAllDAO;
import ru.kolaer.server.dao.entities.DbBirthdayAll;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class DbBirthdayAllDAOImpl implements DbBirthdayAllDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<DbBirthdayAll> getAll() {
		final List<DbBirthdayAll> result = sessionFactory.getCurrentSession().createQuery("from DbBirthdayAll").list();
		return result;
	}
	
	@Override
	public List<DbBirthdayAll> getAllMaxCount(final int count) {
		final List<DbBirthdayAll> result = sessionFactory.getCurrentSession().createQuery("from DbBirthdayAll").setMaxResults(count).list();
		return result;
	}

	@Override
	public int getRowCount() {
		final Number result = (Number)  sessionFactory.getCurrentSession().createQuery("SELECT count(x) FROM DbBirthdayAll x").uniqueResult();
		return result.intValue();
	}

	@Override
	public List<DbBirthdayAll> getUserRangeBirthday(final Date startDate, final Date endDate) {
		final List<DbBirthdayAll> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM DbBirthdayAll t where t.birthday BETWEEN :startDate AND :endDate")
	            .setParameter("startDate", startDate)
	            .setParameter("endDate", endDate)
	            .list();
		return result;
	}
	
	@Override
	public List<DbBirthdayAll> getUsersByBirthday(final Date date) {

		final List<DbBirthdayAll> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM DbBirthdayAll t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
	            .setParameter("date", date)
	            .list();
		return result;
	}

	@Override
	public List<DbBirthdayAll> getUserBirthdayToday() {
		final List<DbBirthdayAll> result = sessionFactory.getCurrentSession().createQuery("FROM DbBirthdayAll t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE)")
	            .list();
		return result;
	}

	@Override
	public int getCountUserBirthday(final Date date) {
		final Number result = (Number) sessionFactory.getCurrentSession().createQuery("SELECT count(t) FROM DbBirthdayAll t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
				.setParameter("date", date)
				.uniqueResult();
		return result.intValue();
	}

	@Override
	public void insertData(final DbBirthdayAll data) {
		sessionFactory.getCurrentSession().persist(data);
		sessionFactory.getCurrentSession().flush();
		sessionFactory.getCurrentSession().clear();
	}

	@Override
	public void insertDataList(final List<DbBirthdayAll> dataList) {
        List<DbBirthdayAll> tempEnqList = dataList;
        for (Iterator<DbBirthdayAll> it = tempEnqList.iterator(); it.hasNext();) {
        	DbBirthdayAll enquiry = it.next();
			sessionFactory.getCurrentSession().persist(enquiry);
			sessionFactory.getCurrentSession().flush();
			sessionFactory.getCurrentSession().clear();
        }	
	}

	@Override
	public List<DbBirthdayAll> getUsersByBirthdayAndOrg(Date date, String organization) {
		final List<DbBirthdayAll> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM DbBirthdayAll t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
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
	public List<DbBirthdayAll> getUsersByInitials(String initials) {
		if(initials == null || initials.isEmpty())
			return Collections.emptyList();
			
		final List<DbBirthdayAll> result = sessionFactory.getCurrentSession().createQuery("FROM DbBirthdayAll t where t.initials like :initials")
				.setParameter("initials", "%" + initials + "%")
				.list();
		return result;
	}

}
