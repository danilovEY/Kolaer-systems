package ru.kolaer.server.employee.model.dto;

import lombok.Data;
import ru.kolaer.common.dto.kolaerweb.TypePostEnum;

/**
 * Created by danilovey on 24.01.2017.
 */
@Data
public class PostRequestDto {
    private String name;
    private String abbreviatedName;
    private Integer rang;
    private TypePostEnum type;
    private String code;
}
