package ru.kolaer.server.employee.converter;

import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.server.employee.model.dto.EducationDto;
import ru.kolaer.server.employee.model.entity.EducationEntity;

import javax.validation.constraints.NotNull;

@Service
@Validated
public class EducationMapper {

    @NotNull
    public EducationDto mapToDto(@NotNull EducationEntity entity) {
        EducationDto dto = new EducationDto();
        dto.setId(entity.getId());
        dto.setDocument(entity.getDocument());
        dto.setQualification(entity.getQualification());
        dto.setDocumentNumber(entity.getDocumentNumber());
        dto.setEmployeeId(entity.getEmployeeId());
        dto.setExpirationDate(entity.getExpirationDate());
        dto.setInstitution(entity.getInstitution());
        dto.setSpecialty(entity.getSpecialty());
        dto.setTypeEducation(entity.getTypeEducation());

        return dto;
    }

}
