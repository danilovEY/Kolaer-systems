package ru.kolaer.server.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.server.employee.model.dto.PunishmentDto;
import ru.kolaer.server.employee.service.PunishmentService;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@Validated
public class PunishmentController {

    private final PunishmentService punishmentService;

    @Autowired
    public PunishmentController(PunishmentService punishmentService) {
        this.punishmentService = punishmentService;
    }

    @GetMapping(RouterConstants.EMPLOYEE_ID_PUNISHMENTS)
    public List<PunishmentDto> getPunishmentsByEmployeeId(
            @PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId
    ) {
        return punishmentService.findPunishmentsByEmployeeId(employeeId);
    }
}
