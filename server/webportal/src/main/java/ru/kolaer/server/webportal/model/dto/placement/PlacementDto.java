package ru.kolaer.server.webportal.model.dto.placement;

import lombok.Data;
import ru.kolaer.common.dto.BaseDto;

@Data
public class PlacementDto implements BaseDto {
    private Long id;
    private String name;
}
