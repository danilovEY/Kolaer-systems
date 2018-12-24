package ru.kolaer.server.webportal.model.dao;

import ru.kolaer.server.webportal.model.entity.achievement.AchievementEntity;

import java.util.List;

public interface AchievementDao extends DefaultDao<AchievementEntity> {
    List<AchievementEntity> findByEmployeeId(long employeeId);
}
