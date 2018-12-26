package ru.kolaer.server.employee.repository;

import org.springframework.stereotype.Repository;
import ru.kolaer.server.core.repository.BaseRepository;
import ru.kolaer.server.employee.model.entity.AchievementEntity;

import java.util.List;

@Repository
public interface AchievementDao extends BaseRepository<AchievementEntity> {
    List<AchievementEntity> findByEmployeeId(long employeeId);
}
