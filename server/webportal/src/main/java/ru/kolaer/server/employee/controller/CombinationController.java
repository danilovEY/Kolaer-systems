package ru.kolaer.server.employee.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.employee.model.dto.CombinationDto;
import ru.kolaer.server.employee.service.CombinationService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class CombinationController {

    private final CombinationService combinationService;

    public CombinationController(CombinationService combinationService) {
        this.combinationService = combinationService;
    }

    @GetMapping(RouterConstants.EMPLOYEES_ID_COMBINATIONS)
    @PreAuthorize("hasAnyRole('" + EmployeeAccessConstant.EMPLOYEE_COMBINATION_READ + "','" + EmployeeAccessConstant.EMPLOYEE_COMBINATION_READ_DEPARTMENT + "')")
    @ApiOperation("Получить достижения сотрудника")
    public List<CombinationDto> findCombinationByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId
    ) {
        return combinationService.findCombinationByEmployeeId(employeeId);
    }

}
