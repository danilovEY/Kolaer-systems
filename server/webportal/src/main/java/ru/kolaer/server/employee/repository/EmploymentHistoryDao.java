package ru.kolaer.server.employee.repository;

import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.entity.EmploymentHistoryEntity;

import java.util.List;

public interface EmploymentHistoryDao extends BaseRepository<EmploymentHistoryEntity> {
    List<EmploymentHistoryEntity> findByEmployeeId(long employeeId);
}
