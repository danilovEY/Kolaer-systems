package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.server.employee.model.dto.PersonalDataDto;
import ru.kolaer.server.employee.service.PersonalDataService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class PersonalDataController {

    private final PersonalDataService personalDataService;

    @Autowired
    public PersonalDataController(PersonalDataService personalDataService) {
        this.personalDataService = personalDataService;
    }

    @GetMapping(RouterConstants.EMPLOYEE_ID_PERSONAL_DATA)
    public List<PersonalDataDto> getPersonalDataByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId
    ) {
        return personalDataService.findPersonalDataByEmployeeId(employeeId);
    }

}
