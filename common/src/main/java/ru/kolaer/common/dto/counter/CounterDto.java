package ru.kolaer.common.dto.counter;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

import java.time.LocalDateTime;

/**
 * Created by danilovey on 25.08.2016.
 */
@Data
public class CounterDto implements BaseDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private String description;
    private boolean displayOnVacation;
}
