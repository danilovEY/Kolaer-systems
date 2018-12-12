package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.webportal.model.entity.vacation.VacationType;

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
