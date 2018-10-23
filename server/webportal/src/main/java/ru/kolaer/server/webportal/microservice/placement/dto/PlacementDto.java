package ru.kolaer.server.webportal.microservice.placement.dto;

import lombok.Data;
import ru.kolaer.common.mvp.model.kolaerweb.BaseDto;

@Data
public class PlacementDto implements BaseDto {
    private Long id;
    private String name;
}
