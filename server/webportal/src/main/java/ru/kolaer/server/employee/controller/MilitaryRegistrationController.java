package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.server.employee.model.dto.MilitaryRegistrationDto;
import ru.kolaer.server.employee.service.MilitaryRegistrationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class MilitaryRegistrationController {

    private final MilitaryRegistrationService militaryRegistrationService;

    @Autowired
    public MilitaryRegistrationController(MilitaryRegistrationService militaryRegistrationService) {
        this.militaryRegistrationService = militaryRegistrationService;
    }

    @GetMapping(RouterConstants.EMPLOYEE_ID_MILITARY_REGISTRATIONS)
    public List<MilitaryRegistrationDto> getMilitaryRegistrationsByEmployeeId(
        @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Valid @Min(1) long employeeId
    ) {
        return militaryRegistrationService.findMilitaryRegistrationsByEmployeeId(employeeId);
    }
}
