package ru.kolaer.server.employee.repository;

import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.entity.PunishmentEntity;

import java.util.List;

public interface PunishmentDao extends BaseRepository<PunishmentEntity> {
    List<PunishmentEntity> findByEmployeeId(long employeeId);
}
