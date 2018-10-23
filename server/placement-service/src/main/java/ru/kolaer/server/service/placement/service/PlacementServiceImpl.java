package ru.kolaer.server.service.placement.service;

import org.springframework.stereotype.Service;
import ru.kolaer.server.service.placement.repository.PlacementRepository;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.service.placement.converter.PlacementConverter;
import ru.kolaer.server.service.placement.dto.PlacementDto;
import ru.kolaer.server.service.placement.entity.PlacementEntity;

@Service
public class PlacementServiceImpl
        extends AbstractDefaultService<PlacementDto, PlacementEntity, PlacementRepository, PlacementConverter>
        implements PlacementService {

    public PlacementServiceImpl(PlacementRepository defaultEntityDao, PlacementConverter converter) {
        super(defaultEntityDao, converter);
    }

}
