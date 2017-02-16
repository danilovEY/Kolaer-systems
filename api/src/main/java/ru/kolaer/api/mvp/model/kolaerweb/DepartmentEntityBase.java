package ru.kolaer.api.mvp.model.kolaerweb;

import lombok.Data;

/**
 * Created by danilovey on 12.09.2016.
 */
@Data
public class DepartmentEntityBase implements DepartmentEntity {
    private Integer Id;
    private String name;
    private String abbreviatedName;
    private Integer chiefEntity;
}
