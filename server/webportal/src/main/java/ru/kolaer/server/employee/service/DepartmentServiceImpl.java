package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.core.exception.NotFoundDataException;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.core.service.AbstractDefaultService;
import ru.kolaer.server.employee.converter.DepartmentConverter;
import ru.kolaer.server.employee.dao.DepartmentDao;
import ru.kolaer.server.employee.model.dto.DepartmentRequestDto;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;
import ru.kolaer.server.employee.model.request.FindDepartmentPageRequest;

import java.util.List;
import java.util.Optional;

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
    @Transactional(readOnly = true)
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
        departmentEntity.setCode(Optional.ofNullable(departmentRequestDto.getCode()).orElse(0));

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
        departmentEntity.setCode(Optional.ofNullable(departmentRequestDto.getCode()).orElse(0));

        return defaultConverter.convertToDto(defaultEntityDao.save(departmentEntity));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepartmentDto> find(FindDepartmentPageRequest request) {
        long count = defaultEntityDao.findCount(request);
        List<DepartmentDto> departments = defaultConverter.convertToDto(defaultEntityDao.find(request));

        return new Page<>(departments, request.getNumber(), count, request.getPageSize());
    }
}
