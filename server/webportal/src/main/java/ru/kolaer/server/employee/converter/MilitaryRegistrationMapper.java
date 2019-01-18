package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.server.employee.model.dto.MilitaryRegistrationDto;
import ru.kolaer.server.employee.model.entity.MilitaryRegistrationEntity;

import javax.validation.constraints.NotNull;

@Service
public class MilitaryRegistrationMapper {

    @NotNull
    public MilitaryRegistrationDto mapToMilitaryRegistrationDto(MilitaryRegistrationEntity entity) {
        MilitaryRegistrationDto dto = new MilitaryRegistrationDto();
        dto.setId(entity.getId());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setRank(entity.getRank());
        dto.setStatusBy(entity.getStatusBy());

        return dto;
    }

}
