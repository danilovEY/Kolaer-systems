package ru.kolaer.server.core.model.dto.upload;

import lombok.Data;
import ru.kolaer.server.employee.model.entity.EmployeeEntity;

@Data
public class UpdatableEmployee {
    private EmployeeEntity employee;
    private String departmentKey;
    private String postKey;

    public UpdatableEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
}
