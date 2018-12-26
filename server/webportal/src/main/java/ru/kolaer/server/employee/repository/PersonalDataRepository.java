package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.PersonalDataEntity;

import java.util.List;

@Repository
public interface PersonalDataRepository extends BaseRepository<PersonalDataEntity> {
    List<PersonalDataEntity> findByEmployeeId(long employeeId);
}
