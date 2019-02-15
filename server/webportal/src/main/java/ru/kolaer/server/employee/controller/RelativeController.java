package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.server.employee.EmployeeAccessConstant;
import ru.kolaer.server.employee.model.dto.RelativeDto;
import ru.kolaer.server.employee.service.RelativeService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class RelativeController {
    private final RelativeService relativeService;

    @Autowired
    public RelativeController(RelativeService relativeService) {
        this.relativeService = relativeService;
    }

    @GetMapping(RouterConstants.EMPLOYEES_ID_RELATIVES)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEE_RELATIVES_GET + "')")
    public List<RelativeDto> getRelativesByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId
    ) {
        return relativeService.findRelatives(employeeId);
    }
}
