package ru.kolaer.server.daotest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.kolaer.server.dao.entities.DbCars;

@Service
public class MyService {
	
	@Autowired
	private DbCarsRepository myTableRepository;
	
	public List<DbCars> getAll() {
		return this.myTableRepository.findAll();
	}
}
