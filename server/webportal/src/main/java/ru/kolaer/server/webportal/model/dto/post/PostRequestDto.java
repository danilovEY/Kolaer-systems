package ru.kolaer.server.webportal.model.dto.post;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.TypePostEnum;

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
