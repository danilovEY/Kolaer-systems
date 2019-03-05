package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.EmploymentHistoryMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.EmploymentHistoryDto;
import ru.kolaer.server.employee.repository.EmploymentHistoryRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class EmploymentHistoryService {

    private final EmploymentHistoryRepository employmentHistoryRepository;
    private final EmploymentHistoryMapper employmentHistoryMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    @Autowired
    public EmploymentHistoryService(EmploymentHistoryRepository employmentHistoryRepository,
            EmploymentHistoryMapper employmentHistoryMapper, AuthenticationService authenticationService,
            EmployeeDao employeeDao
    ) {
        this.employmentHistoryRepository = employmentHistoryRepository;
        this.employmentHistoryMapper = employmentHistoryMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<EmploymentHistoryDto> findEmploymentHistoryByEmployeeId(@Min(1) long employeeId) {

        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_EMPLOYMENT_HISTORIES_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return employmentHistoryRepository.findByEmployeeId(employeeId)
                .stream()
                .map(employmentHistoryMapper::mapToEmploymentHistoryDto)
                .collect(Collectors.toList());
    }
}
