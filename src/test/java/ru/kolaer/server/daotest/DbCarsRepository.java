package ru.kolaer.server.daotest;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.kolaer.server.dao.entities.DbCars;

public interface DbCarsRepository extends JpaRepository<DbCars, Integer> {

}
