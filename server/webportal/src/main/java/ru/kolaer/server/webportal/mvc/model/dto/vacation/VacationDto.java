package ru.kolaer.server.webportal.mvc.model.dto.vacation;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.webportal.mvc.model.entities.vacation.VacationType;

import java.time.LocalDateTime;

@Data
public class VacationDto implements BaseDto {
    private Long id;
    private Long employeeId;
    private String note;
    private LocalDateTime vacationFrom;
    private LocalDateTime vacationTo;
    private Integer days;
    private VacationType vacationType;
}
