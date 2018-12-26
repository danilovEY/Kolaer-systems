package ru.kolaer.server.vacation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VacationTotalCountEntity {
    private long totalCountEmployeeWithBalance;
    private long totalCountEmployeeOnDepartment;
}
