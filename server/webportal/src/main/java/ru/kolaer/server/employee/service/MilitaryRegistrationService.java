package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.MilitaryRegistrationMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.MilitaryRegistrationDto;
import ru.kolaer.server.employee.repository.MilitaryRegistrationRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class MilitaryRegistrationService {
    private final MilitaryRegistrationRepository militaryRegistrationRepository;
    private final MilitaryRegistrationMapper militaryRegistrationMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    public MilitaryRegistrationService(MilitaryRegistrationRepository militaryRegistrationRepository,
            MilitaryRegistrationMapper militaryRegistrationMapper, AuthenticationService authenticationService,
            EmployeeDao employeeDao
    ) {
        this.militaryRegistrationRepository = militaryRegistrationRepository;
        this.militaryRegistrationMapper = militaryRegistrationMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<MilitaryRegistrationDto> findMilitaryRegistrationsByEmployeeId(@Min(1) long employeeId) {
        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_MILITARY_REGISTRATIONS_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return militaryRegistrationRepository
                .findByEmployeeId(employeeId)
                .stream()
                .map(militaryRegistrationMapper::mapToMilitaryRegistrationDto)
                .collect(Collectors.toList());
    }
}
