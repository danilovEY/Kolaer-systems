package ru.kolaer.server.daotest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyTableService {
	
	@Autowired
	private MyTableRepository myTableRepository;
	
	public MyTable getTable(String name){
		return this.myTableRepository.findByName(name);
	}
	
	public List<MyTable> getAll() {
		return this.myTableRepository.findAll();
	}
}
