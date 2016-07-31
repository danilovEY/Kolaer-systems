package ru.kolaer.server.dao.impl;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kolaer.server.dao.DbDataAllDAO;
import ru.kolaer.server.dao.entities.DbDataAll;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DbDataAllDAOImpl implements DbDataAllDAO {
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<DbDataAll> getAll() {
		final List<DbDataAll> result = sessionFactory.getCurrentSession().createQuery("from DbDataAll").list();
		return result;
	}
	
	@Override
	public List<DbDataAll> getAllMaxCount(final int count) {
		final List<DbDataAll> result = sessionFactory.getCurrentSession().createQuery("from DbDataAll").setMaxResults(count).list();
		return result;
	}

	@Override
	public int getRowCount() {
		final Number result = (Number) sessionFactory.getCurrentSession().createQuery("SELECT count(x) FROM DbDataAll x").uniqueResult();
		return result.intValue();
	}

	@Override
	public List<DbDataAll> getUserRangeBirthday(final Date startDate, final Date endDate) {
		final List<DbDataAll> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM DbDataAll t where t.birthday BETWEEN :startDate AND :endDate")
	            .setParameter("startDate", startDate)
	            .setParameter("endDate", endDate)
	            .list();
		return result;
	}
	
	@Override
	public List<DbDataAll> getUsersByBirthday(final Date date) {
		final List<DbDataAll> result = sessionFactory.getCurrentSession().createQuery("SELECT t FROM DbDataAll t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
	            .setParameter("date", date)
	            .list();
		return result;
	}

	@Override
	public List<DbDataAll> getUserBirthdayToday() {
		final List<DbDataAll> result = sessionFactory.getCurrentSession().createQuery("FROM DbDataAll t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE)")
	            .list();
		return result;
	}

	@Override
	public int getCountUserBirthday(final Date date) {
		final Number result = (Number) sessionFactory.getCurrentSession().createQuery("SELECT count(t) FROM DbDataAll t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)")
				.setParameter("date", date)
				.list();
		return result.intValue();
	}

	@Override
	public List<DbDataAll> getUsersByInitials(final String initials) {
		if(initials == null || initials.isEmpty())
			return Collections.emptyList();
		
		final List<DbDataAll> result = sessionFactory.getCurrentSession().createQuery("FROM DbDataAll t where t.initials like :initials")
				.setParameter("initials", "%" + initials + "%")
				.list();
		return result;
	}
}