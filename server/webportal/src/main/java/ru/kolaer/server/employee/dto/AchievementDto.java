package ru.kolaer.server.employee.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

import java.time.LocalDate;

@Getter
@Setter
public class AchievementDto extends DefaultDto {
    private Long employeeId;
    private String name;
    private String orderNumber;
    private LocalDate orderDate;
}
