package ru.kolaer.server.employee.repository;

import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.MilitaryRegistrationEntity;

import java.util.List;

public interface MilitaryRegistrationDao extends BaseRepository<MilitaryRegistrationEntity> {
    List<MilitaryRegistrationEntity> findByEmployeeId(long employeeId);

}
