package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.PersonalDocumentEntity;

import java.util.List;

@Repository
public interface PersonalDocumentRepository extends BaseRepository<PersonalDocumentEntity> {
    List<PersonalDocumentEntity> findByEmployeeId(long employeeId);
}
