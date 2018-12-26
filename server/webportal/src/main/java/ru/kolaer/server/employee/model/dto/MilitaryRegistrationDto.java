package ru.kolaer.server.employee.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

@Getter
@Setter
public class MilitaryRegistrationDto extends DefaultDto {
    private Long employeeId;
    private String rank;
    private String statusBy;
}
