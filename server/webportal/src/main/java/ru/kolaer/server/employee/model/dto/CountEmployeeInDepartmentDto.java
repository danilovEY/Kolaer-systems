package ru.kolaer.server.employee.model.dto;

import lombok.Data;

@Data
public class CountEmployeeInDepartmentDto {
    private long departmentId;
    private String departmentName;
    private long countEmployee;

    public CountEmployeeInDepartmentDto(long departmentId, String departmentName, long countEmployee) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.countEmployee = countEmployee;
    }
}
