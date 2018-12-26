package ru.kolaer.server.employee.model.dto;

import lombok.Getter;
import lombok.Setter;
import ru.kolaer.common.dto.DefaultDto;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class StaffMovementDto extends DefaultDto {
    private Long employeeId;
    private String name;
    private String post;
    private String department;
    private String categoryUnit;
    private BigDecimal salary;
    private Integer surchargeHarmfulness;
    private String cardSlam;
    private String subclassWorkingConditions;
    private String orderNumber;
    private LocalDate orderDate;
    private LocalDate startDate;
    private LocalDate endDate;
}
