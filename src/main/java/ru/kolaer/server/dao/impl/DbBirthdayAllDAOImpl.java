package ru.kolaer.server.dao.impl;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import ru.kolaer.server.dao.DbBirthdayAllDAO;
import ru.kolaer.server.dao.entities.DbBirthdayAll;

public class DbBirthdayAllDAOImpl implements DbBirthdayAllDAO {
	@Inject
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	
	@Override
	public List<DbBirthdayAll> getAll() {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbBirthdayAll> result = entityManager.createQuery("from DbBirthdayAll", DbBirthdayAll.class).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}
	
	@Override
	public List<DbBirthdayAll> getAllMaxCount(final int count) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbBirthdayAll> result = entityManager.createQuery("from DbBirthdayAll", DbBirthdayAll.class).setMaxResults(count).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

	@Override
	public int getRowCount() {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final Number result = (Number) entityManager.createQuery("SELECT count(x) FROM DbBirthdayAll x").getSingleResult();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result.intValue();
	}

	@Override
	public List<DbBirthdayAll> getUserRangeBirthday(final Date startDate, final Date endDate) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbBirthdayAll> result = entityManager.createQuery("SELECT t FROM DbBirthdayAll t where t.birthday BETWEEN :startDate AND :endDate", DbBirthdayAll.class)
	            .setParameter("startDate", startDate, TemporalType.DATE)
	            .setParameter("endDate", endDate, TemporalType.DATE)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}
	
	@Override
	public List<DbBirthdayAll> getUsersByBirthday(final Date date) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();

		final List<DbBirthdayAll> result = entityManager.createQuery("SELECT t FROM DbBirthdayAll t where day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", DbBirthdayAll.class)
	            .setParameter("date", date, TemporalType.DATE)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

	@Override
	public List<DbBirthdayAll> getUserBirthdayToday() {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final List<DbBirthdayAll> result = entityManager.createQuery("FROM DbBirthdayAll t where day(t.birthday) = day(CURRENT_DATE) and month(t.birthday) = month(CURRENT_DATE)", DbBirthdayAll.class)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

	@Override
	public int getCountUserBirthday(final Date date) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final Number result = entityManager.createQuery("SELECT count(t) FROM DbBirthdayAll t where and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", Number.class)
				.setParameter("date", date, TemporalType.DATE)
				.getSingleResult();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result.intValue();
	}

	@Override
	public void insertData(final DbBirthdayAll data) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		entityManager.persist(data);
		entityManager.flush();
		entityManager.clear();
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Override
	public void insertDataList(final List<DbBirthdayAll> dataList) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		
        List<DbBirthdayAll> tempEnqList = dataList;
        for (Iterator<DbBirthdayAll> it = tempEnqList.iterator(); it.hasNext();) {
        	DbBirthdayAll enquiry = it.next();
        	entityManager.persist(enquiry);
        	entityManager.flush();
        	entityManager.clear();
        }	
        
		entityManager.getTransaction().commit();
		entityManager.close();
	}

	@Override
	public List<DbBirthdayAll> getUsersByBirthdayAndOrg(Date date, String organization) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();

		final List<DbBirthdayAll> result = entityManager.createQuery("SELECT t FROM DbBirthdayAll t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", DbBirthdayAll.class)
				.setParameter("org", organization)
				.setParameter("date", date, TemporalType.DATE)
	            .getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

	@Override
	public int getCountUserBirthdayAndOrg(Date date, String organization) {
		final EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		final Number result = entityManager.createQuery("SELECT count(t) FROM DbBirthdayAll t where t.organization = :org and day(t.birthday) = day(:date) and month(t.birthday) = month(:date)", Number.class)
				.setParameter("org", organization)
				.setParameter("date", date, TemporalType.DATE)
				.getSingleResult();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result.intValue();
	}

}
