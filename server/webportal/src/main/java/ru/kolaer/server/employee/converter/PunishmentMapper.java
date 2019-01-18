package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.model.dto.PunishmentDto;
import ru.kolaer.server.employee.model.entity.PunishmentEntity;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class PunishmentMapper {

    @NotNull
    public PunishmentDto mapToPunishmentDto(@NotNull PunishmentEntity entity) {
        PunishmentDto dto = new PunishmentDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setName(entity.getName());
        dto.setOrderDate(entity.getOrderDate());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setStartPunishmentDate(entity.getStartPunishmentDate());

        return dto;
    }

}
