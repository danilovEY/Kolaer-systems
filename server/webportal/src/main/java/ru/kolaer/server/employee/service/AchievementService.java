package ru.kolaer.server.employee.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.server.account.model.dto.AccountAuthorizedDto;
import ru.kolaer.server.core.exception.ForbiddenException;
import ru.kolaer.server.core.service.AuthenticationService;
import ru.kolaer.server.employee.converter.AchievementMapper;
import ru.kolaer.server.employee.dao.EmployeeDao;
import ru.kolaer.server.employee.model.dto.AchievementDto;
import ru.kolaer.server.employee.repository.AchievementRepository;

import javax.validation.constraints.Min;
import java.util.List;

@Service
@Validated
public class AchievementService {
    private final AchievementRepository achievementRepository;
    private final AchievementMapper achievementMapper;
    private final AuthenticationService authenticationService;
    private final EmployeeDao employeeDao;

    public AchievementService(AchievementRepository achievementRepository, AchievementMapper achievementMapper,
            AuthenticationService authenticationService, EmployeeDao employeeDao
    ) {
        this.achievementRepository = achievementRepository;
        this.achievementMapper = achievementMapper;
        this.authenticationService = authenticationService;
        this.employeeDao = employeeDao;
    }

    @Transactional(readOnly = true)
    public List<AchievementDto> findAchievementsByEmployeeId(@Min(1) long employeeId) {
        AccountAuthorizedDto accountAuthorized = authenticationService.getAccountAuthorized();

        if (!accountAuthorized.hasAccess(EmployeeAccessConstant.EMPLOYEE_ACHIEVEMENTS_READ)) {
            if (!employeeDao.employeeEqualDepartment(accountAuthorized.getEmployeeId(), employeeId).isPresent()) {
                throw new ForbiddenException();
            }
        }

        return achievementMapper.mapToDtos(achievementRepository.findByEmployeeId(employeeId));
    }

}
