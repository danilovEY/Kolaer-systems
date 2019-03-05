package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.EducationMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.EducationDto;
import ru.kolaer.server.employee.repository.EducationRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class EducationService {

    private final EducationRepository educationRepository;
    private final EducationMapper educationMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    @Autowired
    public EducationService(EducationRepository educationRepository, EducationMapper educationMapper,
            AuthenticationService authenticationService, EmployeeDao employeeDao
    ) {
        this.educationRepository = educationRepository;
        this.educationMapper = educationMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<EducationDto> findEducations(@Min(1) long employeeId) {

        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_EDUCATIONS_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return educationRepository.findByEmployeeId(employeeId)
                .stream()
                .map(educationMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
