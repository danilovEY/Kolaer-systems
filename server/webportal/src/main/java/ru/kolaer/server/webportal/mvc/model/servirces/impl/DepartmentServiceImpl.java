package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.server.webportal.mvc.model.converter.DepartmentConverter;
import ru.kolaer.server.webportal.mvc.model.dao.DepartmentDao;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartmentService;

/**
 * Created by danilovey on 12.09.2016.
 */
@Service
public class DepartmentServiceImpl extends AbstractDefaultService<DepartmentDto, DepartmentEntity, DepartmentDao, DepartmentConverter> implements DepartmentService {

    protected DepartmentServiceImpl(DepartmentDao departmentDao, DepartmentConverter converter) {
        super(departmentDao, converter);
    }

    @Override
    public DepartmentDto getGeneralDepartmentEntityByName(String name) {
        if(name == null || name.trim().isEmpty()) {
            return null;
        }

        return defaultConverter.convertToDto(this.defaultEntityDao.findByName(name));
    }

}
