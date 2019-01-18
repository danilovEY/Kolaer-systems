package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import ru.kolaer.server.employee.model.dto.PersonalDataDto;
import ru.kolaer.server.employee.model.entity.PersonalDataEntity;

import javax.validation.constraints.NotNull;

@Service
public class PersonalDataMapper {

    @NotNull
    public PersonalDataDto mapPersonalDataDto(@NotNull PersonalDataEntity entity) {
        PersonalDataDto dto = new PersonalDataDto();
        dto.setId(entity.getId());
        dto.setAddressRegistration(entity.getAddressRegistration());
        dto.setAddressResidential(entity.getAddressResidential());
        dto.setDisabilityDate(entity.getDisabilityDate());
        dto.setDisabilityGroup(entity.getDisabilityGroup());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setMaritalStatus(entity.getMaritalStatus());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setPlaceOfBirth(entity.getPlaceOfBirth());

        return dto;
    }
}
