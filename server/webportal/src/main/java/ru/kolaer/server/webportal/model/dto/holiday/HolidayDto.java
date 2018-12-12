package ru.kolaer.server.webportal.model.dto.holiday;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;
import ru.kolaer.api.mvp.model.kolaerweb.TypeDay;

import java.time.LocalDate;

/**
 * Created by danilovey on 31.10.2016.
 */
@Data
public class HolidayDto implements BaseDto {
    private Long id;
    private String name;
    private LocalDate holidayDate;
    private TypeDay holidayType;
}
