package ru.kolaer.server.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MyTableRepository extends JpaRepository<MyTable, Integer> {
	@Query("select b from MyTable b where b.name = :name")
	MyTable findByName(@Param("name") String name);
}
