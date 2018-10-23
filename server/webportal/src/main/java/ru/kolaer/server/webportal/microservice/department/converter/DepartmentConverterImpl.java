package ru.kolaer.server.webportal.microservice.department.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.common.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.server.webportal.microservice.department.entity.DepartmentEntity;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
public class DepartmentConverterImpl implements DepartmentConverter {

    @Override
    public DepartmentEntity convertToModel(DepartmentDto dto) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setId(dto.getId());
        departmentEntity.setName(dto.getName());
        departmentEntity.setAbbreviatedName(departmentEntity.getAbbreviatedName());
        departmentEntity.setChiefEmployeeId(dto.getChiefId());
        departmentEntity.setCode(dto.getCode());

        return departmentEntity;
    }

    @Override
    public DepartmentDto convertToDto(DepartmentEntity model) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setAbbreviatedName(model.getAbbreviatedName());
        departmentDto.setName(model.getName());
        departmentDto.setId(model.getId());
        departmentDto.setCode(model.getCode());
        return departmentDto;
    }

    @Override
    public DepartmentDto updateData(DepartmentDto oldDto, DepartmentEntity newModel) {
        oldDto.setAbbreviatedName(newModel.getAbbreviatedName());
        oldDto.setName(newModel.getName());
        oldDto.setId(newModel.getId());
        oldDto.setCode(newModel.getCode());
        return oldDto;
    }
}
