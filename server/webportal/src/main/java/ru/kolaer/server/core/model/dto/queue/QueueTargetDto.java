package ru.kolaer.server.core.model.dto.queue;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

@Data
public class QueueTargetDto implements BaseDto {
    private Long id;
    private String name;
}
