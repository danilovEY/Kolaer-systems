package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationType;

import java.time.LocalDate;

@Data
public class VacationDto implements BaseDto {
    private Long id;
    private Long employeeId;
    private String note;
    private LocalDate vacationFrom;
    private LocalDate vacationTo;
    private Integer vacationDays;
    private VacationType vacationType;
}
