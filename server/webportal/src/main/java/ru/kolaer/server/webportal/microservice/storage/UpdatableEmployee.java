package ru.kolaer.server.webportal.microservice.storage;

import lombok.Data;
import ru.kolaer.server.webportal.microservice.employee.EmployeeEntity;

@Data
public class UpdatableEmployee {
    private EmployeeEntity employee;
    private String departmentKey;
    private String postKey;

    public UpdatableEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
}
