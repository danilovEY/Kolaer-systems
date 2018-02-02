package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.converter.PassportConverter;
import ru.kolaer.server.webportal.mvc.model.dao.PassportDao;
import ru.kolaer.server.webportal.mvc.model.dto.PassportDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.PassportEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.PassportService;

/**
 * Created by danilovey on 12.10.2017.
 */
@Service
public class PassportServiceImpl extends AbstractDefaultService<PassportDto, PassportEntity, PassportDao, PassportConverter> implements PassportService {
    protected PassportServiceImpl(PassportDao defaultEntityDao, PassportConverter converter) {
        super(defaultEntityDao, converter);
    }
}
