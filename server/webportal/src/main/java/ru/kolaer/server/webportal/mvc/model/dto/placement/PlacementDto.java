package ru.kolaer.server.webportal.mvc.model.dto.placement;

import lombok.Data;
import ru.kolaer.api.mvp.model.kolaerweb.BaseDto;

@Data
public class PlacementDto implements BaseDto {
    private Long id;
    private String name;
}
