package ru.kolaer.server.dao.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kolaer.server.dao.DbDataAllDAO;
import ru.kolaer.server.dao.entities.DbDataAll;

@Service
@Transactional
public class DbDataAllDAOImpl implements DbDataAllDAO {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public List<DbDataAll> getAll() {
		final List<DbDataAll> result = entityManager.createQuery("from DbDataAll", DbDataAll.class).getResultList();
		return result;
	}
	
	@Override
	public List<DbDataAll> getAllMaxCount(final int count) {
		final List<DbDataAll> result = entityManager.createQuery("from DbDataAll", DbDataAll.class).setMaxResults(count).getResultList();
		return result;
	}

	@Override
	public int getRowCount() {
		final Number result = (Number) entityManager.createQuery("SELECT count(x) FROM DbDataAll x").getSingleResult();
		return result.intValue();
	}

	@Override
	public List<DbDataAll> getUserRangeBirthday(final Date startDate, final Date endDate) {
		final List<DbDataAll> result = entityManager.createQuery("SELECT t FROM DbDataAll t where and t.birthday BETWEEN :startDate AND :endDate", DbDataAll.class)
	            .setParameter("startDate", startDate, TemporalType.DATE)
	            .setParameter("endDate", endDate, TemporalType.DATE)
	            .getResultList();
		return result;
	}
	
	@Override
	public List<DbDataAll> getUsersByBirthday(final Date date) {
		final List<DbDataAll> result = entityManager.createQuery("SELECT t FROM DbDataAll t where and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", DbDataAll.class)
	            .setParameter("date", date, TemporalType.DATE)
	            .getResultList();
		return result;
	}

	@Override
	public List<DbDataAll> getUserBirthdayToday() {
		final List<DbDataAll> result = entityManager.createQuery("FROM DbDataAll t where and day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE)", DbDataAll.class)
	            .getResultList();
		return result;
	}

	@Override
	public int getCountUserBirthday(final Date date) {
		final Number result = entityManager.createQuery("SELECT count(t) FROM DbDataAll t where and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", Number.class)
				.setParameter("date", date, TemporalType.DATE)
				.getSingleResult();
		return result.intValue();
	}

	@Override
	public List<DbDataAll> getUsersByInitials(String initials) {
		final List<DbDataAll> result = entityManager.createQuery("FROM DbDataAll t where t.initials like :initials", DbDataAll.class)
				.setParameter("initials", "%" + initials + "%")
				.getResultList();
		return result;
	}

}
