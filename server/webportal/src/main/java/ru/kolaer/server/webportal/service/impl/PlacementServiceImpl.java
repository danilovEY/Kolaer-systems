package ru.kolaer.server.webportal.service.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.webportal.model.converter.PlacementConverter;
import ru.kolaer.server.webportal.model.dao.PlacementDao;
import ru.kolaer.server.webportal.model.dto.placement.PlacementDto;
import ru.kolaer.server.webportal.model.entity.placement.PlacementEntity;
import ru.kolaer.server.webportal.service.PlacementService;

@Service
public class PlacementServiceImpl
        extends AbstractDefaultService<PlacementDto, PlacementEntity, PlacementDao, PlacementConverter>
        implements PlacementService {

    public PlacementServiceImpl(PlacementDao defaultEntityDao, PlacementConverter converter) {
        super(defaultEntityDao, converter);
    }

}
