package ru.kolaer.server.webportal.microservice.typework;

import org.springframework.stereotype.Service;
import ru.kolaer.common.mvp.model.kolaerweb.typework.TypeWorkDto;

@Service
public class TypeWorkConverterImpl implements TypeWorkConverter {
    @Override
    public TypeWorkEntity convertToModel(TypeWorkDto dto) {
        if (dto == null) {
            return null;
        }

        TypeWorkEntity typeWorkEntity = new TypeWorkEntity();
        typeWorkEntity.setId(dto.getId());
        typeWorkEntity.setName(dto.getName());
        return typeWorkEntity;
    }

    @Override
    public TypeWorkDto convertToDto(TypeWorkEntity model) {
        return updateData(new TypeWorkDto(), model);
    }

    @Override
    public TypeWorkDto updateData(TypeWorkDto oldDto, TypeWorkEntity newModel) {
        if (oldDto == null || newModel == null) {
            return null;
        }

        oldDto.setId(newModel.getId());
        oldDto.setName(newModel.getName());

        return oldDto;
    }
}
