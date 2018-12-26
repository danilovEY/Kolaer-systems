package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.EmploymentHistoryEntity;

import java.util.List;

@Repository
public interface EmploymentHistoryRepository extends BaseRepository<EmploymentHistoryEntity> {
    List<EmploymentHistoryEntity> findByEmployeeId(long employeeId);
}
