package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.CombinationMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.CombinationDto;
import ru.kolaer.server.employee.repository.CombinationRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class CombinationService {
    private final CombinationRepository combinationRepository;
    private final CombinationMapper combinationMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    @Autowired
    public CombinationService(CombinationRepository combinationRepository, CombinationMapper combinationMapper,
            AuthenticationService authenticationService, EmployeeDao employeeDao
    ) {
        this.combinationRepository = combinationRepository;
        this.combinationMapper = combinationMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<CombinationDto> findCombinationByEmployeeId(@Min(1) long employeeId) {

        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_COMBINATION_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return combinationRepository.findByEmployeeId(employeeId)
                .stream()
                .map(combinationMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
