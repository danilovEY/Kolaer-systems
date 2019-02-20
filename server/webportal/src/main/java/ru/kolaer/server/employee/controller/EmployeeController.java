package ru.kolaer.server.employee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kolaer.common.constant.PathVariableConstants;
import ru.kolaer.common.constant.RouterConstants;
import ru.kolaer.common.constant.assess.EmployeeAccessConstant;
import ru.kolaer.common.dto.Page;
import ru.kolaer.common.dto.employee.EmployeeDto;
import ru.kolaer.server.core.model.dto.ResultUpdate;
import ru.kolaer.server.core.model.dto.holiday.HistoryChangeDto;
import ru.kolaer.server.core.service.UpdatableEmployeeService;
import ru.kolaer.server.core.service.UpdateEmployeesService;
import ru.kolaer.server.employee.model.dto.EmployeeRequestDto;
import ru.kolaer.server.employee.model.dto.UpdateTypeWorkEmployeeRequestDto;
import ru.kolaer.server.employee.model.request.FindEmployeePageRequest;
import ru.kolaer.server.employee.service.EmployeeService;

import javax.validation.constraints.Min;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 * Рест контроллер для работы с сотрудниками.
 */
@RestController
@Api(tags = "Сотрудники КолАЭР", description = "Сотрудники из организации КолАЭР.")
@Slf4j
@Validated
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UpdateEmployeesService updateEmployeesService;
    private final UpdatableEmployeeService updatableEmployeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              @Qualifier("updateEmployeesServiceImpl") UpdateEmployeesService updateEmployeesService,
                              @Qualifier("generateReportForOldDbService") UpdatableEmployeeService updatableEmployeeService) {
        this.employeeService = employeeService;
        this.updateEmployeesService = updateEmployeesService;
        this.updatableEmployeeService = updatableEmployeeService;
    }

    @ApiOperation("Получить сотрудника")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEE_GET + "')")
    @GetMapping(RouterConstants.EMPLOYEES_ID)
    public EmployeeDto getAllEmployees(@PathVariable(PathVariableConstants.EMPLOYEE_ID) @Min(1) long employeeId) {
        return this.employeeService.getById(employeeId);
    }

    @ApiOperation("Добавить сотрудника")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEES_ADD + "')")
    @PostMapping(RouterConstants.EMPLOYEES)
    public EmployeeDto createEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
        return this.employeeService.add(employeeRequestDto);
    }

    @ApiOperation("Обновить сотрудника")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEE_EDIT + "')")
    @PutMapping(RouterConstants.EMPLOYEES_ID)
    public EmployeeDto updateEmployee(@PathVariable(PathVariableConstants.EMPLOYEE_ID) Long employeeId,
            @RequestBody EmployeeRequestDto employeeRequestDto) {
        return this.employeeService.update(employeeId, employeeRequestDto);
    }

    @ApiOperation("Изменить вид работы сотрудника")
    @PutMapping(RouterConstants.EMPLOYEES_ID_TYPE_WORK)
    @PreAuthorize("hasAnyRole('" + EmployeeAccessConstant.EMPLOYEE_TYPE_WORK_EDIT_DEPARTMENT + "')")
    public EmployeeDto updateEmployee(@PathVariable(PathVariableConstants.EMPLOYEE_ID) Long employeeId,
            @RequestBody UpdateTypeWorkEmployeeRequestDto request) {
        return this.employeeService.updateWorkType(employeeId, request);
    }

    @ApiOperation("Удалить сотрудника")
    @DeleteMapping(RouterConstants.EMPLOYEES_ID)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEE_DELETE + "')")
    public void deleteEmployee(@PathVariable(PathVariableConstants.EMPLOYEE_ID) Long employeeId) {
        this.employeeService.delete(employeeId);
    }

    @ApiOperation(value = "Получить всех сотрудников")
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEES_GET + "')")
    @GetMapping(RouterConstants.EMPLOYEES)
    public Page<EmployeeDto> getAllEmployees(@ModelAttribute FindEmployeePageRequest request) {
        return this.employeeService.getEmployees(request);
    }

    @ApiOperation(
            value = "Получить всех сотрудников (между датами)",
            notes = "Получить всех сотрудников у кого день рождение между датами"
    )
    @GetMapping(RouterConstants.EMPLOYEES + "/get/birthday/{startDate}/{endDate}") //TODO: Need refactoring
    public List<EmployeeDto> getUsersRangeBirthday(
            final @ApiParam(value = "Дата с", required = true) @PathVariable String startDate,
            final @ApiParam(value = "Дата по", required = true) @PathVariable String endDate) throws ParseException {
        final SimpleDateFormat sdf = startDate.contains("-")
                ? new SimpleDateFormat("yyyy-MM-dd")
                : new SimpleDateFormat("dd.MM.yyyy");

        final Date startDateParse = sdf.parse(startDate);
        final Date endDateParse = sdf.parse(endDate);
        return employeeService.getUserRangeBirthday(startDateParse, endDateParse);
    }

    @ApiOperation(
            value = "Получить всех сотрудников (сегодня)",
            notes = "Получить всех сотрудников у кого сегодня день рождение"
    )
    @GetMapping(RouterConstants.EMPLOYEES + "/get/birthday/today") //TODO: Need refactoring
    public List<EmployeeDto> getUsersRangeBirthday() {
        return this.employeeService.getUserBirthdayToday();
    }


    @ApiOperation(
            value = "Получить всех сотрудников (в определенную дату)",
            notes = "Получить всех сотрудников у кого день рождение в определенную дату"
    )
    @GetMapping(RouterConstants.EMPLOYEES + "/get/birthday/{date}") //TODO: Need refactoring
    public List<EmployeeDto> getUsersRangeBirthday(
           final @ApiParam(value = "Дата", required = true) @PathVariable String date) throws ParseException {
        final SimpleDateFormat sdf = date.contains("-")
                ? new SimpleDateFormat("yyyy-MM-dd")
                : new SimpleDateFormat("dd.MM.yyyy");

        final Date dateParse = sdf.parse(date);
        return this.employeeService.getUsersByBirthday(dateParse);
    }

    @ApiOperation(
            value = "Получить количество сотрудников (в определенную датату)",
            notes = "Получить количество сотрудников у кого день рождение в определенную датату"
    )
    @GetMapping(RouterConstants.EMPLOYEES + "/get/birthday/{date}/count") //TODO: Need refactoring
    public int getCountUsersBirthday(
            final @ApiParam(value = "Дата", required = true) @PathVariable String date) throws ParseException {
        final SimpleDateFormat sdf = date.contains("-")
                ? new SimpleDateFormat("yyyy-MM-dd")
                : new SimpleDateFormat("dd.MM.yyyy");

        final Date dateParse = sdf.parse(date);
        return this.employeeService.getCountUserBirthday(dateParse);
    }

    @PostMapping(RouterConstants.EMPLOYEES_SYNC)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEES_SYNC + "')")
    @ApiOperation(value = "Обновить сотрудников КолАЭР из xlsx")
    public List<HistoryChangeDto> uploadEmployee(@RequestParam("file")MultipartFile file) throws IOException {
        return updateEmployeesService.updateEmployees(file.getInputStream());
    }

    @PostMapping(RouterConstants.EMPLOYEES_REPORT_OLD)
    @PreAuthorize("hasRole('" + EmployeeAccessConstant.EMPLOYEES_REPORT_OLD + "')")
    @ApiOperation(value = "Сформировать выгрузку для старой базы")
    public void generateAndSendReportForOldDb() {
        updatableEmployeeService.updateEmployee(new ResultUpdate());
    }
}
