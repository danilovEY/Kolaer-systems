package ru.kolaer.server.placement.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.server.core.model.dto.PaginationRequest;
import ru.kolaer.server.core.model.dto.placement.PlacementDto;
import ru.kolaer.server.placement.service.PlacementService;

import javax.validation.constraints.NotNull;


@Api(tags = "Помещения")
@RestController
@Validated
public class PlacementController {

    private final PlacementService placementService;

    @Autowired
    public PlacementController(PlacementService placementService) {
        this.placementService = placementService;
    }

    @ApiOperation(value = "Получить все помещения")
    @GetMapping(RouterConstants.PLACEMENT)
    public PageDto<PlacementDto> getAllPlacement(@ModelAttribute @NotNull PaginationRequest request) {
        return placementService.findAll(request);
    }
}
