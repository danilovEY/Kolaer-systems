package ru.kolaer.server.core.model.dto.holiday;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;
import ru.kolaer.common.dto.kolaerweb.TypeDay;

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
