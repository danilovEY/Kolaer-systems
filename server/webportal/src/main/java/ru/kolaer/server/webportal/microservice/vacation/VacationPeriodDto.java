package ru.kolaer.server.webportal.microservice.vacation;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.BaseDto;

@Data
public class VacationPeriodDto implements BaseDto {
    private Long id;
    private int year;
    private VacationPeriodStatus status;

}
