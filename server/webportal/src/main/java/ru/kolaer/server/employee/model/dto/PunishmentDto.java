package ru.kolaer.server.employee.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@Getter
@Setter
public class PunishmentDto extends DefaultDto {
    private Long employeeId;
    private String name;
    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate startPunishmentDate;
}
