package ru.kolaer.server.dao.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import ru.kolaer.server.dao.DbDataAllDAO;
import ru.kolaer.server.dao.entities.DbDataAll;

public class DbDataAllDAOImpl implements DbDataAllDAO {
	@Inject
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	
	@Override
	public List<DbDataAll> getAll() {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbDataAll> result = entityManager.createQuery("from DbDataAll", DbDataAll.class).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}
	
	@Override
	public List<DbDataAll> getAllMaxCount(final int count) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbDataAll> result = entityManager.createQuery("from DbDataAll", DbDataAll.class).setMaxResults(count).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

	@Override
	public int getRowCount() {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final Number result = (Number) entityManager.createQuery("SELECT count(x) FROM DbDataAll x").getSingleResult();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result.intValue();
	}

	@Override
	public List<DbDataAll> getUserRangeBirthday(Date startDate, Date endDate) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbDataAll> result = entityManager.createQuery("SELECT t FROM DbDataAll t where t.birthday BETWEEN :startDate AND :endDate", DbDataAll.class)
	            .setParameter("startDate", startDate, TemporalType.DATE)
	            .setParameter("endDate", endDate, TemporalType.DATE)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}
	
	@Override
	public List<DbDataAll> getUsersByBirthday(Date date) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbDataAll> result = entityManager.createQuery("SELECT t FROM DbDataAll t where t.birthday = :date", DbDataAll.class)
	            .setParameter("date", date, TemporalType.DATE)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

	@Override
	public List<DbDataAll> getUserBirthdayToday() {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbDataAll> result = entityManager.createQuery("FROM DbDataAll t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE) ", DbDataAll.class)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

}
