package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by danilovey on 12.09.2016.
 */
@Data
@NoArgsConstructor
public class DepartmentRequestDto {
    private String name;
    private String abbreviatedName;
    private Integer code;
}
