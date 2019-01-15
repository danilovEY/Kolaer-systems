package ru.kolaer.server.employee.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@Getter
@Setter
public class EducationDto extends DefaultDto {
    private Long employeeId;
    private String typeEducation;
    private String institution;
    private String specialty;
    private String qualification;
    private String document;
    private String documentNumber;
    private LocalDate expirationDate;
}
