package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.PersonalDocumentMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.PersonalDocumentDto;
import ru.kolaer.server.employee.repository.PersonalDocumentRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PersonalDocumentService {
    private final PersonalDocumentRepository personalDocumentRepository;
    private final PersonalDocumentMapper personalDocumentMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    @Autowired
    public PersonalDocumentService(PersonalDocumentRepository personalDocumentRepository,
            PersonalDocumentMapper personalDocumentMapper, AuthenticationService authenticationService,
            EmployeeDao employeeDao
    ) {
        this.personalDocumentRepository = personalDocumentRepository;
        this.personalDocumentMapper = personalDocumentMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<PersonalDocumentDto> findPersonalDocumentByEmployeeId(@Min(1) long employeeId) {

        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_PERSONAL_DOCUMENT_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return personalDocumentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(personalDocumentMapper::convertToDto)
                .collect(Collectors.toList());
    }

}
