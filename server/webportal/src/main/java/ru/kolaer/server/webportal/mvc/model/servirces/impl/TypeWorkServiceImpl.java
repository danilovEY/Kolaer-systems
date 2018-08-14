package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.server.webportal.mvc.model.converter.TypeWorkConverter;
import ru.kolaer.server.webportal.mvc.model.dao.TypeWorkDao;
import ru.kolaer.server.webportal.mvc.model.dto.typework.TypeWorkDto;
import ru.kolaer.server.webportal.mvc.model.entities.typework.TypeWorkEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.TypeWorkService;

@Service
public class TypeWorkServiceImpl
        extends AbstractDefaultService<TypeWorkDto, TypeWorkEntity, TypeWorkDao, TypeWorkConverter>
        implements TypeWorkService {

    public TypeWorkServiceImpl(TypeWorkDao defaultEntityDao, TypeWorkConverter converter) {
        super(defaultEntityDao, converter);
    }

}
