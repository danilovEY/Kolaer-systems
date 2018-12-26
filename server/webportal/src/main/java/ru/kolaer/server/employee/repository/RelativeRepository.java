package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.RelativeEntity;

import java.util.List;

@Repository
public interface RelativeRepository extends BaseRepository<RelativeEntity> {
    List<RelativeEntity> findByEmployeeId(long employeeId);
}
