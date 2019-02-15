package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.model.dto.CombinationDto;
import ru.kolaer.server.employee.model.entity.CombinationEntity;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class CombinationMapper {

    @NotNull
    public CombinationDto convertToDto(@NotNull CombinationEntity entity) {
        CombinationDto combinationDto = new CombinationDto();
        combinationDto.setId(entity.getId());
        combinationDto.setEmployeeId(entity.getEmployeeId());
        combinationDto.setEndDate(entity.getEndDate());
        combinationDto.setPost(entity.getPost());
        combinationDto.setStartDate(entity.getStartDate());

        return combinationDto;
    }

}
