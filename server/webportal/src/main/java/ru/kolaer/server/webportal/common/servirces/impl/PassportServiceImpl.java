package ru.kolaer.server.webportal.common.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.common.servirces.PassportService;
import ru.kolaer.server.webportal.mvc.model.converter.PassportConverter;
import ru.kolaer.server.webportal.mvc.model.dao.PassportRepository;
import ru.kolaer.server.webportal.common.dto.passport.PassportDto;
import ru.kolaer.server.webportal.common.entities.general.PassportEntity;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
public class PassportServiceImpl extends AbstractDefaultService<PassportDto, PassportEntity, PassportRepository, PassportConverter> implements PassportService {
    protected PassportServiceImpl(PassportRepository defaultEntityDao, PassportConverter converter) {
        super(defaultEntityDao, converter);
    }
}
