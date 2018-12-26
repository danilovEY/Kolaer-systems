package ru.kolaer.server.core.model.dto.placement;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

@Data
public class PlacementDto implements BaseDto {
    private Long id;
    private String name;
}
