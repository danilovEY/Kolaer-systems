package ru.kolaer.server.webportal.common.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.microservice.placement.PlacementService;
import ru.kolaer.server.webportal.microservice.placement.PlacementConverter;
import ru.kolaer.server.webportal.microservice.placement.PlacementRepository;
import ru.kolaer.server.webportal.microservice.placement.PlacementDto;
import ru.kolaer.server.webportal.microservice.placement.PlacementEntity;

@Service
public class PlacementServiceImpl
        extends AbstractDefaultService<PlacementDto, PlacementEntity, PlacementRepository, PlacementConverter>
        implements PlacementService {

    public PlacementServiceImpl(PlacementRepository defaultEntityDao, PlacementConverter converter) {
        super(defaultEntityDao, converter);
    }

}
