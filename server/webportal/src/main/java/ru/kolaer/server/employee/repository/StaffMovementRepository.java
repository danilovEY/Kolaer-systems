package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.StaffMovementEntity;

import java.util.List;

@Repository
public interface StaffMovementRepository extends BaseRepository<StaffMovementEntity> {
    List<StaffMovementEntity> findByEmployeeId(long employeeId);
}
