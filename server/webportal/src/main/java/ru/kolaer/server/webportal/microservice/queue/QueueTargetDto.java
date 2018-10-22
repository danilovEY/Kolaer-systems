package ru.kolaer.server.webportal.microservice.queue;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.BaseDto;

@Data
public class QueueTargetDto implements BaseDto {
    private Long id;
    private String name;
}
