package ru.kolaer.server.dao.impl;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import ru.kolaer.server.dao.DbUser1сDAO;
import ru.kolaer.server.dao.entities.DbUsers1c;

public class DbUser1cDAOImpl implements DbUser1сDAO {
	@Inject
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	
	@Override
	public List<DbUsers1c> getAll() {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbUsers1c> result = entityManager.createQuery("from DbUsers1c", DbUsers1c.class).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}
	
	@Override
	public List<DbUsers1c> getAllMaxCount(final int count) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbUsers1c> result = entityManager.createQuery("from DbUsers1c", DbUsers1c.class).setMaxResults(count).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

	@Override
	public int getRowCount() {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final Number result = (Number) entityManager.createQuery("SELECT count(x) FROM DbUsers1c x").getSingleResult();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result.intValue();
	}

	@Override
	public List<DbUsers1c> getUserRangeBirthday(Date startDate, Date endDate) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbUsers1c> result = entityManager.createQuery("SELECT t FROM DbUsers1c t where t.birthday BETWEEN :startDate AND :endDate", DbUsers1c.class)
	            .setParameter("startDate", startDate, TemporalType.DATE)
	            .setParameter("endDate", endDate, TemporalType.DATE)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}
	
	@Override
	public List<DbUsers1c> getUsersByBirthday(Date date) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbUsers1c> result = entityManager.createQuery("SELECT t FROM DbUsers1c t where t.birthday = :date", DbUsers1c.class)
	            .setParameter("date", date, TemporalType.DATE)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

}
