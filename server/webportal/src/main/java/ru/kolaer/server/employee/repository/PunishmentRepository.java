package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.PunishmentEntity;

import java.util.List;

@Repository
public interface PunishmentRepository extends BaseRepository<PunishmentEntity> {
    List<PunishmentEntity> findByEmployeeId(long employeeId);
}
