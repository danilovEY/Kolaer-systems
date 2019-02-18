package ru.kolaer.server.placement.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.dto.Page;
import ru.kolaer.server.core.model.dto.placement.PlacementDto;
import ru.kolaer.server.core.model.dto.placement.PlacementFilter;
import ru.kolaer.server.core.model.dto.placement.PlacementSort;
import ru.kolaer.server.placement.service.PlacementService;


@Api(tags = "Помещения")
@RestController
@RequestMapping(value = "/placement")
public class PlacementController {

    private final PlacementService placementService;

    @Autowired
    public PlacementController(PlacementService placementService) {
        this.placementService = placementService;
    }

    @ApiOperation(value = "Получить все помещения")
    @PreAuthorize("permitAll()")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<PlacementDto> getAllPlacement(@RequestParam(value = "page", defaultValue = "1") Integer number,
                                              @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                              PlacementSort sortParam,
                                              PlacementFilter filter) {
        return placementService.getAll(sortParam, filter, number, pageSize);
    }
}
