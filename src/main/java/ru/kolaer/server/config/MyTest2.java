package ru.kolaer.server.config;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import ru.kolaer.server.dao.entities.DbCars;

public class MyTest2 {
	
	public static void main(String[] args) {
		AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext(KolaerServerConfig.class);
		
		LocalContainerEntityManagerFactoryBean sessionFactory = context.getBean(LocalContainerEntityManagerFactoryBean.class);
		EntityManager entityManager = sessionFactory.getObject().createEntityManager();
		entityManager.getTransaction().begin();
		List<DbCars> result = entityManager.createQuery("from DbCars", DbCars.class).getResultList();
		for(DbCars car : result)
		{
			System.out.println(car.getAuto());
		}
		entityManager.getTransaction().commit();
		entityManager.close();

	}

}
