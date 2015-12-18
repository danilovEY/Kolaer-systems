package ru.kolaer.server.restful.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ru.kolaer.server.dao.DbCarDAO;
import ru.kolaer.server.dao.entities.DbCar;

@RestController
@RequestMapping(value="/dao")
public class DataBaseObjects {
	
	@Autowired
	private DbCarDAO dbCarDAO;
	
	
	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public List<DbCar> getData() {
		
		/*EntityManager entityManager = entityManagerFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		List<DbCars> result = entityManager.createQuery("from DbCars", DbCars.class).getResultList();
		entityManager.getTransaction().commit();
		entityManager.close();*/
		
		return dbCarDAO.getAll();
	}
	
}
