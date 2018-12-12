package ru.kolaer.server.webportal.model.dto.vacation;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.server.webportal.model.entity.vacation.VacationPeriodStatus;

@Data
public class VacationPeriodDto implements BaseDto {
    private Long id;
    private int year;
    private VacationPeriodStatus status;

}
