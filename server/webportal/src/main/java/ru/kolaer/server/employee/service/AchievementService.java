package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import ru.kolaer.server.employee.repository.AchievementDao;

@Service
public class AchievementService {
    private final AchievementDao achievementDao;

    public AchievementService(AchievementDao achievementDao) {
        this.achievementDao = achievementDao;
    }


}
