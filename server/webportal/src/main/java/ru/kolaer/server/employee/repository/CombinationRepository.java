package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.CombinationEntity;

import java.util.List;

@Repository
public interface CombinationRepository extends BaseRepository<CombinationEntity> {
    List<CombinationEntity> findByEmployeeId(long employeeId);
}
