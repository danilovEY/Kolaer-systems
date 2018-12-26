package ru.kolaer.server.vacation.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VacationTotalCountDepartmentEntity {
    private long departmentId;
    private long totalCountEmployeeWithBalance;
    private long totalCountEmployeeOnDepartment;
}
