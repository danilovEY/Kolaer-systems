package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.server.employee.model.dto.AchievementDto;
import ru.kolaer.server.employee.service.AchievementService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping(RouterConstants.EMPLOYEE_ID_ACHIEVEMENT)
    public List<AchievementDto> findAchievementByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Valid @Min(1) long employeeId
    ) {
        return achievementService.findAchievementsByEmployeeId(employeeId);
    }

}
