package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.kolaer.common.dto.PageDto;
import ru.kolaer.common.dto.kolaerweb.DepartmentDto;
import ru.kolaer.server.core.converter.CommonConverter;
import ru.kolaer.server.core.exception.NotFoundDataException;
import ru.kolaer.server.core.exception.UnexpectedRequestParams;
import ru.kolaer.server.employee.converter.DepartmentConverter;
import ru.kolaer.server.employee.model.dto.DepartmentRequestDto;
import ru.kolaer.server.employee.model.entity.DepartmentEntity;
import ru.kolaer.server.employee.model.request.FindDepartmentPageRequest;
import ru.kolaer.server.employee.repository.DepartmentRepository;
import ru.kolaer.server.employee.repository.DepartmentSpecifications;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by danilovey on 12.09.2016.
 */
@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final DepartmentConverter departmentConverter;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository, DepartmentConverter departmentConverter) {
        this.departmentRepository = departmentRepository;
        this.departmentConverter = departmentConverter;
    }

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

        return departmentConverter.convertToDto(departmentRepository.save(departmentEntity));
    }

    @Transactional
    public DepartmentDto update(Long depId, DepartmentRequestDto departmentRequestDto) {
        if(departmentRequestDto == null ||
                StringUtils.isEmpty(departmentRequestDto.getName()) ||
                StringUtils.isEmpty(departmentRequestDto.getAbbreviatedName())) {
            throw new UnexpectedRequestParams("Имя не должно быть пустым");
        }

        DepartmentEntity departmentEntity = departmentRepository.findById(depId)
                .orElseThrow(() -> new NotFoundDataException("Подразделение не найдено"));

        departmentEntity.setAbbreviatedName(departmentRequestDto.getAbbreviatedName());
        departmentEntity.setName(departmentRequestDto.getName());
        departmentEntity.setCode(Optional.ofNullable(departmentRequestDto.getCode()).orElse(0));

        return departmentConverter.convertToDto(departmentRepository.save(departmentEntity));
    }

    @Transactional(readOnly = true)
    public PageDto<DepartmentDto> find(FindDepartmentPageRequest request) {
        Page<DepartmentEntity> page = departmentRepository.findAll(
                DepartmentSpecifications.findAll(request),
                request.toPageRequest(request.getDirection(), request.getSort().getFieldName())
        );

        return CommonConverter.toPageDto(page, departmentConverter::convertToDto);
    }

    @Transactional
    public void delete(@Min(1) long departmentId) {
        departmentRepository.findById(departmentId)
                .map(dep -> {
                    dep.setDeleted(true);
                    return dep;
                }).ifPresent(departmentRepository::save);
    }

    @Transactional(readOnly = true)
    public List<DepartmentDto> getById(@NotNull Collection<Long> ids) {
        return departmentConverter.convertToDto(departmentRepository.findAllById(ids));
    }

    @Transactional(readOnly = true)
    public Optional<DepartmentDto> getById(@Min(1) long id) {
        return departmentRepository.findById(id)
                .map(departmentConverter::convertToDto);
    }
}
