package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.PunishmentMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.PunishmentDto;
import ru.kolaer.server.employee.repository.PunishmentRepository;

import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Validated
public class PunishmentService {
    private final PunishmentRepository punishmentRepository;
    private final PunishmentMapper punishmentMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    public PunishmentService(PunishmentRepository punishmentRepository,
            PunishmentMapper punishmentMapper, AuthenticationService authenticationService, EmployeeDao employeeDao) {
        this.punishmentRepository = punishmentRepository;
        this.punishmentMapper = punishmentMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<PunishmentDto> findPunishmentsByEmployeeId(@Min(1) long employeeId) {

        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_PUNISHMENTS_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return punishmentRepository.findByEmployeeId(employeeId)
                .stream()
                .map(punishmentMapper::mapToPunishmentDto)
                .collect(Collectors.toList());
    }
}
