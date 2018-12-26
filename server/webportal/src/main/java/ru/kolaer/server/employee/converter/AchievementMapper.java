package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.model.dto.AchievementDto;
import ru.kolaer.server.employee.model.entity.AchievementEntity;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class AchievementMapper {

    public @NotNull AchievementDto mapToDto(@NotNull AchievementEntity entity) {
        AchievementDto achievementDto = new AchievementDto();
        achievementDto.setId(entity.getId());
        achievementDto.setEmployeeId(entity.getEmployeeId());
        achievementDto.setName(entity.getName());
        achievementDto.setOrderDate(entity.getOrderDate());
        achievementDto.setOrderNumber(entity.getOrderNumber());
        return achievementDto;
    }

    public @NotNull List<AchievementDto> mapToDtos(@NotNull List<AchievementEntity> entities) {
        return entities
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public @NotNull AchievementEntity mapToEntity(@NotNull AchievementDto dto) {
        AchievementEntity achievementEntity = new AchievementEntity();
        achievementEntity.setId(dto.getId());
        achievementEntity.setEmployeeId(dto.getEmployeeId());
        achievementEntity.setName(dto.getName());
        achievementEntity.setOrderDate(dto.getOrderDate());
        achievementEntity.setOrderNumber(dto.getOrderNumber());
        return achievementEntity;
    }

    public @NotNull List<AchievementEntity> mapToEntities(@NotNull List<AchievementDto> dtos) {
        return dtos
                .stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());
    }
}
