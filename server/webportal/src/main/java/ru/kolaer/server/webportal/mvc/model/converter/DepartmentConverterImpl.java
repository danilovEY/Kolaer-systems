package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.DepartmentDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.DepartmentEntity;

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

        return departmentEntity;
    }

    @Override
    public DepartmentDto convertToDto(DepartmentEntity model) {
        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setAbbreviatedName(model.getAbbreviatedName());
        departmentDto.setName(model.getName());
        departmentDto.setId(model.getId());
        return departmentDto;
    }

    @Override
    public DepartmentDto updateData(DepartmentDto oldDto, DepartmentEntity newModel) {
        oldDto.setAbbreviatedName(newModel.getAbbreviatedName());
        oldDto.setName(newModel.getName());
        oldDto.setId(newModel.getId());
        return oldDto;
    }
}