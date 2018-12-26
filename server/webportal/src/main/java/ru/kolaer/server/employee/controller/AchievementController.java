package ru.kolaer.server.employee.controller;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.common.constant.PathVariables;
import ru.kolaer.common.constant.Routers;
import ru.kolaer.server.employee.model.dto.AchievementDto;
import ru.kolaer.server.employee.service.AchievementService;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
public class AchievementController {

    private final AchievementService achievementService;

    @Autowired
    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @GetMapping(Routers.EMPLOYEE_ID_ACHIEVEMENT)
    public List<AchievementDto> findAchievementByEmployeeId(
            @PathVariable(PathVariables.EMPLOYEE_ID) @Validated @Min(1) long employeeId) {
        return achievementService.findAchievementsByEmployeeId(employeeId);
    }

    @PostMapping(Routers.EMPLOYEE_ID_ACHIEVEMENT)
    public List<AchievementDto> findAchievementByEmployeeId(
            @PathVariable(PathVariables.EMPLOYEE_ID) @Validated @Min(1) long employeeId, @Validated @RequestBody Test test) {
        return achievementService.findAchievementsByEmployeeId(employeeId);
    }

}

@Data
class Test {
    @NotNull
    private String myField;
}
