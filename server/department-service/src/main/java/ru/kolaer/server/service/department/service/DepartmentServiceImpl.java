package ru.kolaer.server.service.department.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.common.mvp.model.kolaerweb.Page;
import ru.kolaer.server.service.department.dto.DepartmentRequestDto;
import ru.kolaer.server.service.department.dto.FindDepartmentPageRequest;
import ru.kolaer.server.webportal.common.exception.NotFoundDataException;
import ru.kolaer.server.webportal.common.exception.UnexpectedRequestParams;
import ru.kolaer.server.webportal.common.servirces.AbstractDefaultService;
import ru.kolaer.server.service.department.converter.DepartmentConverter;
import ru.kolaer.server.service.department.entity.DepartmentEntity;
import ru.kolaer.server.service.department.repository.DepartmentRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 12.09.2016.
 */
@Service
public class DepartmentServiceImpl
        extends AbstractDefaultService<DepartmentDto, DepartmentEntity, DepartmentRepository, DepartmentConverter>
        implements DepartmentService {

    protected DepartmentServiceImpl(DepartmentRepository departmentDao, DepartmentConverter converter) {
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
