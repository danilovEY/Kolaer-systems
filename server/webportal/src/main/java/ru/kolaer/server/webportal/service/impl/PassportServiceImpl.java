package ru.kolaer.server.webportal.service.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.webportal.model.converter.PassportConverter;
import ru.kolaer.server.webportal.model.dao.PassportDao;
import ru.kolaer.server.webportal.model.dto.passport.PassportDto;
import ru.kolaer.server.webportal.model.entity.general.PassportEntity;
import ru.kolaer.server.webportal.service.PassportService;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
public class PassportServiceImpl extends AbstractDefaultService<PassportDto, PassportEntity, PassportDao, PassportConverter> implements PassportService {
    protected PassportServiceImpl(PassportDao defaultEntityDao, PassportConverter converter) {
        super(defaultEntityDao, converter);
    }
}
