package ru.kolaer.server.webportal.model.servirce.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.model.converter.PlacementConverter;
import ru.kolaer.server.webportal.model.dao.PlacementDao;
import ru.kolaer.server.webportal.model.dto.placement.PlacementDto;
import ru.kolaer.server.webportal.model.entity.placement.PlacementEntity;
import ru.kolaer.server.webportal.model.servirce.AbstractDefaultService;
import ru.kolaer.server.webportal.model.servirce.PlacementService;

@Service
public class PlacementServiceImpl
        extends AbstractDefaultService<PlacementDto, PlacementEntity, PlacementDao, PlacementConverter>
        implements PlacementService {

    public PlacementServiceImpl(PlacementDao defaultEntityDao, PlacementConverter converter) {
        super(defaultEntityDao, converter);
    }

}
