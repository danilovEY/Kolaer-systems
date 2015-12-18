package ru.kolaer.server.dao;

import ru.kolaer.server.dao.entities.DbCar;

public interface DbCarDAO extends DbDAO<DbCar>{
	void addCar(DbCar car);
	void removeCar(DbCar car);
	DbCar getCarByID(Integer id);
}
