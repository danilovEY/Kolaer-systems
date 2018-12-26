package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.EducationEntity;

import java.util.List;

@Repository
public interface EducationRepository extends BaseRepository<EducationEntity> {
    List<EducationEntity> findByEmployeeId(long employeeId);
}
