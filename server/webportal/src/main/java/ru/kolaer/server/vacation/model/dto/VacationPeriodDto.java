package ru.kolaer.server.vacation.model.dto;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.server.vacation.model.entity.VacationPeriodStatus;

@Data
public class VacationPeriodDto implements BaseDto {
    private Long id;
    private int year;
    private VacationPeriodStatus status;

}
