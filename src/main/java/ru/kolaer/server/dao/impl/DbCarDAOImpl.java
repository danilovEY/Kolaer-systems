package ru.kolaer.server.dao.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Repository;

import ru.kolaer.server.dao.DbCarDAO;
import ru.kolaer.server.dao.entities.DbCar;

@Repository
public class DbCarDAOImpl implements DbCarDAO{

	@Inject
	private LocalContainerEntityManagerFactoryBean entityManagerFactory;
	
	@Override
	public List<DbCar> getAll() {
		
		EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		List<DbCar> result = entityManager.createQuery("from DbCar", DbCar.class).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();
		return result;
	}

	@Override
	public void addCar(DbCar car) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeCar(DbCar car) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public DbCar getCarByID(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

}
