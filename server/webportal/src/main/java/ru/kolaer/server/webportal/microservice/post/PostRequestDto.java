package ru.kolaer.server.webportal.microservice.post;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.TypePostEnum;

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
