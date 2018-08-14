package ru.kolaer.server.webportal.mvc.model.dto.typework;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

@Data
public class TypeWorkDto implements BaseDto {
    private Long id;
    private String name;
}
