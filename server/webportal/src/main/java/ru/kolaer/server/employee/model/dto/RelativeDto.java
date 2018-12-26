package ru.kolaer.server.employee.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@Getter
@Setter
public class RelativeDto extends DefaultDto {
    private Long employeeId;
    private String initials;
    private LocalDate dateOfBirth;
}
