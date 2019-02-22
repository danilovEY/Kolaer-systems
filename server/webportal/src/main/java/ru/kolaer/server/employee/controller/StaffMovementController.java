package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.employee.model.dto.StaffMovementDto;
import ru.kolaer.server.employee.service.StaffMovementService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class StaffMovementController {

    private final StaffMovementService staffMovementService;

    @Autowired
    public StaffMovementController(StaffMovementService staffMovementService) {
        this.staffMovementService = staffMovementService;
    }

    @GetMapping(RouterConstants.EMPLOYEES_ID_STAFF_MOVEMENTS)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEE_STAFF_MOVEMENTS_READ + "')")
    public List<StaffMovementDto> findStaffMovementByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId
    ) {
        return staffMovementService.findStaffMovementsByEmployeeId(employeeId);
    }
}
