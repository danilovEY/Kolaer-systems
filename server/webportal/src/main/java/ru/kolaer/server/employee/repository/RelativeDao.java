package ru.kolaer.server.employee.repository;

import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.RelativeEntity;

import java.util.List;

public interface RelativeDao extends BaseRepository<RelativeEntity> {
    List<RelativeEntity> findByEmployeeId(long employeeId);
}
