package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.converter.AchievementMapper;
import ru.kolaer.server.employee.model.dto.AchievementDto;
import ru.kolaer.server.employee.repository.AchievementRepository;

import javax.validation.constraints.Min;
import java.util.List;

@Service
@Validated
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;

    public AchievementService(AchievementRepository achievementRepository,
                              AchievementMapper achievementMapper) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
    }

    public List<AchievementDto> findAchievementsByEmployeeId(@Min(1) long employeeId) {
        return achievementMapper.mapToDtos(achievementRepository.findByEmployeeId(employeeId));
    }

}
