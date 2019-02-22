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
import ru.kolaer.server.employee.model.dto.PersonalDocumentDto;
import ru.kolaer.server.employee.service.PersonalDocumentService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class PersonalDocumentController {
    private final PersonalDocumentService personalDocumentService;

    @Autowired
    public PersonalDocumentController(PersonalDocumentService personalDocumentService) {
        this.personalDocumentService = personalDocumentService;
    }

    @GetMapping(RouterConstants.EMPLOYEES_ID_PERSONAL_DOCUMENTS)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEE_PERSONAL_DOCUMENT_READ + "')")
    public List<PersonalDocumentDto> findPersonalDocumentByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId
    ) {
        return personalDocumentService.findPersonalDocumentByEmployeeId(employeeId);
    }
}
