package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.converter.PlacementConverter;
import ru.kolaer.server.webportal.mvc.model.dao.PlacementDao;
import ru.kolaer.server.webportal.mvc.model.dto.placement.PlacementDto;
import ru.kolaer.server.webportal.mvc.model.entities.placement.PlacementEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.PlacementService;

@Service
public class PlacementServiceImpl
        extends AbstractDefaultService<PlacementDto, PlacementEntity, PlacementDao, PlacementConverter>
        implements PlacementService {

    public PlacementServiceImpl(PlacementDao defaultEntityDao, PlacementConverter converter) {
        super(defaultEntityDao, converter);
    }

}
