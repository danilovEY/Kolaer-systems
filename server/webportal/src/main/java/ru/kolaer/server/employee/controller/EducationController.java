package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.server.employee.model.dto.EducationDto;
import ru.kolaer.server.employee.service.EducationService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class EducationController {

    private final EducationService educationService;

    @Autowired
    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    @GetMapping(RouterConstants.EMPLOYEE_ID_EDUCATION)
    public List<EducationDto> getEducations(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Valid @Min(1) long employeeId
    ) {
        return educationService.findEducations(employeeId);
    }
}
