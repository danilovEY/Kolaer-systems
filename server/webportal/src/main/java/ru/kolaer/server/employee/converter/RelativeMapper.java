package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.model.dto.RelativeDto;
import ru.kolaer.server.employee.model.entity.RelativeEntity;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class RelativeMapper {

    @NotNull
    public RelativeDto mapToRelativeDto(@NotNull RelativeEntity entity) {
        RelativeDto dto = new RelativeDto();
        dto.setId(entity.getId());
        dto.setDateOfBirth(entity.getDateOfBirth());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setInitials(entity.getInitials());
        dto.setRelationDegree(entity.getRelationDegree());

        return dto;
    }

}
