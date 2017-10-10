package ru.kolaer.server.webportal.mvc.model.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.api.mvp.model.kolaerweb.RoleDto;
import ru.kolaer.server.webportal.mvc.model.entities.general.RoleEntity;

import java.util.Optional;

/**
 * Created by danilovey on 09.10.2017.
 */
@Service
public class RoleConverterImpl implements RoleConverter {

    @Override
    public RoleEntity convertToModel(RoleDto dto) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(dto.getId());
        roleEntity.setType(dto.getType());

        Optional.ofNullable(dto.getAccountId())
                .ifPresent(roleEntity::setAccountId);

        return roleEntity;
    }

    @Override
    public RoleDto convertToDto(RoleEntity model) {
        RoleDto roleDto = new RoleDto();
        roleDto.setType(model.getType());
        roleDto.setId(model.getId());

        //TODO: account

        return roleDto;
    }

    @Override
    public RoleDto updateData(RoleDto oldDto, RoleEntity newModel) {
        oldDto.setType(newModel.getType());
        oldDto.setId(newModel.getId());

        //TODO: account

        return oldDto;
    }
}
