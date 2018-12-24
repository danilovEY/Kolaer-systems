package ru.kolaer.common.dto.kolaerweb;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.kolaer.common.dto.BaseDto;

/**
 * Created by danilovey on 12.09.2016.
 */
@Data
@NoArgsConstructor
public class DepartmentDto implements BaseDto {
    private Long id;
    private String name;
    private String abbreviatedName;
    private Long chiefId;
    private int code;

    public DepartmentDto(Long id) {
        this.id = id;
    }
}
