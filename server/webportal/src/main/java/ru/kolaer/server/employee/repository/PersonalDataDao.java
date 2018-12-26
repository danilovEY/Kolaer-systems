package ru.kolaer.server.employee.repository;

import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.PersonalDataEntity;

import java.util.List;

public interface PersonalDataDao extends BaseRepository<PersonalDataEntity> {
    List<PersonalDataEntity> findByEmployeeId(long employeeId);
}
