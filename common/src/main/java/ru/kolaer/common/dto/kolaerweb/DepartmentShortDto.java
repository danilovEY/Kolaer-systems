package ru.kolaer.common.dto.kolaerweb;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

/**
 * Created by danilovey on 09.10.2017.
 */
@Data
public class DepartmentShortDto implements BaseDto {
    private Long id;
    private String name;
}
