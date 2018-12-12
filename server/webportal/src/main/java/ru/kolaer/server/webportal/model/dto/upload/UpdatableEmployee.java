package ru.kolaer.server.webportal.model.dto.upload;

import lombok.Data;
import ru.kolaer.server.webportal.model.entity.general.EmployeeEntity;

@Data
public class UpdatableEmployee {
    private EmployeeEntity employee;
    private String departmentKey;
    private String postKey;

    public UpdatableEmployee(EmployeeEntity employee) {
        this.employee = employee;
    }
}
