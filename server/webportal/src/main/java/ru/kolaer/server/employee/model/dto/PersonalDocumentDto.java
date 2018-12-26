package ru.kolaer.server.employee.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@Getter
@Setter
public class PersonalDocumentDto extends DefaultDto {
    private Long employeeId;
    private String name;
    private LocalDate dateOfIssue;
    private String documentNumber;
    private String issuedBy;
}
