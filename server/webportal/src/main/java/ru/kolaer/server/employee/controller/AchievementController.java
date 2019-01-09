package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariables;
import ru.kolaer.common.constant.Routers;
import ru.kolaer.server.core.annotation.UrlDeclaration;
import ru.kolaer.server.employee.model.dto.AchievementDto;
import ru.kolaer.server.employee.service.AchievementService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @UrlDeclaration(description = "Получить достижения сотрудника по ID")
    @GetMapping(Routers.EMPLOYEE_ID_ACHIEVEMENT)
    public List<AchievementDto> findAchievementByEmployeeId(
            @PathVariable(PathVariables.EMPLOYEE_ID) @Validated @Min(1) long employeeId) {
        return achievementService.findAchievementsByEmployeeId(employeeId);
    }

}
