package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.server.employee.model.dto.EmploymentHistoryDto;
import ru.kolaer.server.employee.service.EmploymentHistoryService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class EmploymentHistoryController {
    private final EmploymentHistoryService employmentHistoryService;

    @Autowired
    public EmploymentHistoryController(EmploymentHistoryService employmentHistoryService) {
        this.employmentHistoryService = employmentHistoryService;
    }

    @GetMapping(RouterConstants.EMPLOYEE_ID_EMPLOYMENT_HISTORIES)
    public List<EmploymentHistoryDto> getEmploymentHistoriesByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Valid @Min(1) long employeeId
    ) {
        return employmentHistoryService.findEmploymentHistoryByEmployeeId(employeeId);
    }
}
