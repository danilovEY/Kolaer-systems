package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by danilovey on 12.09.2016.
 */
@Data
public class DepartmentDto implements BaseDto {
    private Long id;
    private String name;
    private String abbreviatedName;
    private Long chiefId;
}
