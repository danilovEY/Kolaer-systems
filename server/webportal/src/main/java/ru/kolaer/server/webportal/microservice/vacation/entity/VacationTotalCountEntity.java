package ru.kolaer.server.webportal.microservice.vacation.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VacationTotalCountEntity {
    private long totalCountEmployeeWithBalance;
    private long totalCountEmployeeOnDepartment;
}
