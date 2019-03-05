package ru.kolaer.server.employee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.StaffMovementMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.StaffMovementDto;
import ru.kolaer.server.employee.repository.StaffMovementRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class StaffMovementService {
    private final StaffMovementRepository staffMovementRepository;
    private final StaffMovementMapper staffMovementMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    @Autowired
    public StaffMovementService(StaffMovementRepository staffMovementRepository, StaffMovementMapper staffMovementMapper, AuthenticationService authenticationService, EmployeeDao employeeDao) {
        this.staffMovementRepository = staffMovementRepository;
        this.staffMovementMapper = staffMovementMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<StaffMovementDto> findStaffMovementsByEmployeeId(@Min(1) long employeeId) {

        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_STAFF_MOVEMENTS_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return staffMovementRepository.findByEmployeeId(employeeId)
                .stream()
                .map(staffMovementMapper::convertToDto)
                .collect(Collectors.toList());
    }
}
