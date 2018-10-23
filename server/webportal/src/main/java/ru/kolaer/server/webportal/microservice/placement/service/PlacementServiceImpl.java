package ru.kolaer.server.webportal.microservice.placement.service;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.microservice.placement.converter.PlacementConverter;
import ru.kolaer.server.webportal.microservice.placement.dto.PlacementDto;
import ru.kolaer.server.webportal.microservice.placement.entity.PlacementEntity;
import ru.kolaer.server.webportal.microservice.placement.repository.PlacementRepository;

@Service
public class PlacementServiceImpl
        extends AbstractDefaultService<PlacementDto, PlacementEntity, PlacementRepository, PlacementConverter>
        implements PlacementService {

    public PlacementServiceImpl(PlacementRepository defaultEntityDao, PlacementConverter converter) {
        super(defaultEntityDao, converter);
    }

}
