package ru.kolaer.server.service.placement.dto;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.BaseDto;

@Data
public class PlacementDto implements BaseDto {
    private Long id;
    private String name;
}
