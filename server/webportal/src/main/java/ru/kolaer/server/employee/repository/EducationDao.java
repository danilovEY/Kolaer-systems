package ru.kolaer.server.employee.repository;

import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.EducationEntity;

import java.util.List;

public interface EducationDao extends BaseRepository<EducationEntity> {
    List<EducationEntity> findByEmployeeId(long employeeId);
}
