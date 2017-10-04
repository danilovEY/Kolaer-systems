package ru.kolaer.api.mvp.model.kolaerweb.jpac;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

/**
 * Created by danilovey on 06.09.2016.
 */
@Data
public class TypeViolationDto implements BaseDto{
    private Long id;
    private String name;
}
