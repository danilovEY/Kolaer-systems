package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.model.dto.PersonalDocumentDto;
import ru.kolaer.server.employee.model.entity.PersonalDocumentEntity;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class PersonalDocumentMapper {

    @NotNull
    public PersonalDocumentDto convertToDto(@NotNull PersonalDocumentEntity entity) {
        PersonalDocumentDto personalDocumentDto = new PersonalDocumentDto();
        personalDocumentDto.setId(entity.getId());
        personalDocumentDto.setEmployeeId(entity.getEmployeeId());
        personalDocumentDto.setDateOfIssue(entity.getDateOfIssue());
        personalDocumentDto.setDocumentNumber(entity.getDocumentNumber());
        personalDocumentDto.setIssuedBy(entity.getIssuedBy());

        return personalDocumentDto;
    }

}
