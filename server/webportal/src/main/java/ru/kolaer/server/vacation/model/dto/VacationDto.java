package ru.kolaer.server.vacation.model.dto;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.server.vacation.model.entity.VacationType;

import java.time.LocalDate;

@Data
public class VacationDto implements BaseDto {
    private Long id;
    private long employeeId;
    private String note;
    private LocalDate vacationFrom;
    private LocalDate vacationTo;
    private int vacationDays;
    private VacationType vacationType = VacationType.PAID_HOLIDAY;
}
