package ru.kolaer.server.webportal.mvc.model.dto;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

@Data
public class QueueTargetDto implements BaseDto {
    private Long id;
    private String name;
}
