package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.PersonalDataMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.PersonalDataDto;
import ru.kolaer.server.employee.repository.PersonalDataRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PersonalDataService {
    private final PersonalDataRepository personalDataRepository;
    private final PersonalDataMapper personalDataMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    @Autowired
    public PersonalDataService(PersonalDataRepository personalDataRepository,
            PersonalDataMapper personalDataMapper, AuthenticationService authenticationService,
            EmployeeDao employeeDao
    ) {
        this.personalDataRepository = personalDataRepository;
        this.personalDataMapper = personalDataMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<PersonalDataDto> findPersonalDataByEmployeeId(@Min(1) long employeeId) {

        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_PERSONAL_DATA_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return personalDataRepository.findByEmployeeId(employeeId)
                .stream()
                .map(personalDataMapper::mapPersonalDataDto)
                .collect(Collectors.toList());
    }

}
