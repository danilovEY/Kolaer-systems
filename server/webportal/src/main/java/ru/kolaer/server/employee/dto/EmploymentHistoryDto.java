package ru.kolaer.server.employee.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@Getter
@Setter
public class EmploymentHistoryDto extends DefaultDto {
    private Long employeeId;
    private String organization;
    private String post;
    private LocalDate startWorkDate;
    private LocalDate endWorkDate;
}
