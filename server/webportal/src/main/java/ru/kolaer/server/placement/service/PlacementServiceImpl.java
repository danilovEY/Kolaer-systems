package ru.kolaer.server.placement.service;

import org.springframework.stereotype.Service;
import ru.kolaer.server.core.model.dto.placement.PlacementDto;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.placement.dao.PlacementDao;
import ru.kolaer.server.placement.model.entity.PlacementEntity;

@Service
public class PlacementServiceImpl
        extends AbstractDefaultService<PlacementDto, PlacementEntity, PlacementDao, PlacementConverter>
        implements PlacementService {

    public PlacementServiceImpl(PlacementDao defaultEntityDao, PlacementConverter converter) {
        super(defaultEntityDao, converter);
    }

}
