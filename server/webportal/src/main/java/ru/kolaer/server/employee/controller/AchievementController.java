package ru.kolaer.server.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.server.employee.EmployeeAccessConstant;
import ru.kolaer.server.employee.model.dto.AchievementDto;
import ru.kolaer.server.employee.service.AchievementService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Api(tags = "Достижения")
@Validated
public class AchievementController {
    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping(RouterConstants.EMPLOYEES_ID_ACHIEVEMENTS)
    @PreAuthorize("hasAnyRole('" + EmployeeAccessConstant.EMPLOYEE_ACHIEVEMENTS_GET + "')")
    @ApiOperation("Получить достижения сотрудника")
    public List<AchievementDto> findAchievementByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId
    ) {
        return achievementService.findAchievementsByEmployeeId(employeeId);
    }

}
