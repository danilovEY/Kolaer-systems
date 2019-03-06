package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;

@Repository
public interface DepartmentRepository extends BaseRepository<DepartmentEntity> {

}
