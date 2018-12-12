package ru.kolaer.server.webportal.model.entity.vacation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VacationTotalCountDepartmentEntity {
    private long departmentId;
    private long totalCountEmployeeWithBalance;
    private long totalCountEmployeeOnDepartment;
}
