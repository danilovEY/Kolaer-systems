package ru.kolaer.server.webportal.microservice.sync;

import lombok.Data;
import ru.kolaer.server.webportal.microservice.employee.entity.EmployeeEntity;

@Data
public class UpdatableEmployee {
    private EmployeeEntity employee;
    private String departmentKey;
    private String postKey;

    public UpdatableEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
}
