package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by danilovey on 09.10.2017.
 */
@Data
public class DepartmentShortDto implements BaseDto{
    private Long id;
    private String name;
}
