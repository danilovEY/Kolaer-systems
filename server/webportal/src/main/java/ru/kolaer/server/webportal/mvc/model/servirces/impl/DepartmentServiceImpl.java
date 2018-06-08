package ru.kolaer.server.webportal.mvc.model.servirces.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.server.webportal.exception.NotFoundDataException;
import ru.kolaer.server.webportal.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.mvc.model.converter.DepartmentConverter;
import ru.kolaer.server.webportal.mvc.model.dao.DepartmentDao;
import ru.kolaer.server.webportal.mvc.model.dto.DepartmentRequestDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;
import ru.kolaer.server.webportal.mvc.model.servirces.AbstractDefaultService;
import ru.kolaer.server.webportal.mvc.model.servirces.DepartmentService;

/**
 * Created by danilovey on 12.09.2016.
 */
@Service
public class DepartmentServiceImpl
        extends AbstractDefaultService<DepartmentDto, DepartmentEntity, DepartmentDao, DepartmentConverter>
        implements DepartmentService {

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

    @Override
    @Transactional
    public DepartmentDto add(DepartmentRequestDto departmentRequestDto) {
        if(departmentRequestDto == null ||
                StringUtils.isEmpty(departmentRequestDto.getName()) ||
                StringUtils.isEmpty(departmentRequestDto.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("Имя не должно быть пустым");
        }

        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setAbbreviatedName(departmentRequestDto.getAbbreviatedName());
        departmentEntity.setName(departmentRequestDto.getName());

        return defaultConverter.convertToDto(defaultEntityDao.save(departmentEntity));
    }

    @Override
    @Transactional
    public DepartmentDto update(Long depId, DepartmentRequestDto departmentRequestDto) {
        if(departmentRequestDto == null ||
                StringUtils.isEmpty(departmentRequestDto.getName()) ||
                StringUtils.isEmpty(departmentRequestDto.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("Имя не должно быть пустым");
        }

        DepartmentEntity departmentEntity = defaultEntityDao.findById(depId);
        if(departmentEntity == null) {
            throw new NotFoundDataException("Подразделение не найдено");
        }

        departmentEntity.setAbbreviatedName(departmentRequestDto.getAbbreviatedName());
        departmentEntity.setName(departmentRequestDto.getName());

        return defaultConverter.convertToDto(defaultEntityDao.save(departmentEntity));
    }
}
