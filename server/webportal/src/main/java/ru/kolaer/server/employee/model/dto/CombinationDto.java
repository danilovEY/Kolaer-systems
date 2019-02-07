package ru.kolaer.server.employee.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
public class CombinationDto extends DefaultDto {

    private Long employeeId;
    private String post;
    private LocalDate startDate;
    private LocalDate endDate;

}
