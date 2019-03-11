package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.MilitaryRegistrationEntity;

import java.util.Optional;

@Repository
public interface MilitaryRegistrationRepository extends BaseRepository<MilitaryRegistrationEntity> {

    Optional<MilitaryRegistrationEntity> findByEmployeeId(long employeeId);

}
