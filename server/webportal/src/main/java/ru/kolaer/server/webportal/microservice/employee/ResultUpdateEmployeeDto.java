package ru.kolaer.server.webportal.microservice.employee;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danilovey on 26.01.2017.
 */
@Data
public class ResultUpdateEmployeeDto {
    private List<EmployeeEntity> deleteEmployee = new ArrayList<>();
    private List<EmployeeEntity> addEmployee = new ArrayList<>();
}
