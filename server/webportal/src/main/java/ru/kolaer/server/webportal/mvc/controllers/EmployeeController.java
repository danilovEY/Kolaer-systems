package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kolaer.api.mvp.model.kolaerweb.EmployeeDto;
import ru.kolaer.api.mvp.model.kolaerweb.Page;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dto.EmployeeFilter;
import ru.kolaer.server.webportal.mvc.model.dto.EmployeeRequestDto;
import ru.kolaer.server.webportal.mvc.model.dto.EmployeeSort;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;
import ru.kolaer.server.webportal.mvc.model.servirces.UpdateEmployeesService;

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
@RequestMapping(value = "/employees")
@Api(tags = "Сотрудники КолАЭР", description = "Сотрудники из организации КолАЭР.")
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UpdateEmployeesService updateEmployeesService;

    @Autowired
    public EmployeeController(EmployeeService employeeService,
                              @Qualifier("updateEmployeesServiceImpl") UpdateEmployeesService updateEmployeesService) {
        this.employeeService = employeeService;
        this.updateEmployeesService = updateEmployeesService;
    }

    @ApiOperation(
            value = "Получить всех сотрудников"
    )
    @UrlDeclaration(description = "Получить всех сотрудников", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<EmployeeDto> getAllEmployees(@RequestParam(value = "page", defaultValue = "0") Integer number,
                                             @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize,
                                             EmployeeSort sortParam,
                                             EmployeeFilter filter) {
        return this.employeeService.getAll(sortParam, filter, number, pageSize);
    }

    @ApiOperation(value = "Получить сотрудника")
    @UrlDeclaration(description = "Получить сотрудника", isAccessAll = true)
    @RequestMapping(value = "/{employeeId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public EmployeeDto getAllEmployees(@PathVariable(value = "employeeId") Long employeeId) {
        return this.employeeService.getById(employeeId);
    }

    @ApiOperation(value = "Добавить сотрудника")
    @UrlDeclaration(description = "Добавить сотрудника", isUser = false, requestMethod = RequestMethod.POST)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public EmployeeDto createEmployee(@RequestBody EmployeeRequestDto employeeRequestDto) {
        return this.employeeService.add(employeeRequestDto);
    }

    @ApiOperation(value = "Обновить сотрудника")
    @UrlDeclaration(description = "Обновить сотрудника", isUser = false, requestMethod = RequestMethod.PUT)
    @RequestMapping(value = "/{employeeId}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public EmployeeDto updateEmployee(@PathVariable(value = "employeeId") Long employeeId,
                                      @RequestBody EmployeeRequestDto employeeRequestDto) {
        return this.employeeService.update(employeeId, employeeRequestDto);
    }

    @ApiOperation(value = "Удалить сотрудника")
    @UrlDeclaration(description = "Удалить сотрудника", isUser = false, requestMethod = RequestMethod.DELETE)
    @RequestMapping(value = "/{employeeId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void deleteEmployee(@PathVariable(value = "employeeId") Long employeeId) {
        this.employeeService.delete(employeeId);
    }

    @ApiOperation(
            value = "Получить всех сотрудников из подразделения по ID"
    )
    @UrlDeclaration(description = "Получить всех сотрудников из подразделения", isAccessAll = true)
    @RequestMapping(value = "/get/all/by/dep", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Page<EmployeeDto> getAllEmployeesByDep(
            @ApiParam("Подразделение") @RequestParam(value = "id") Long id,
            @RequestParam(value = "page", defaultValue = "0") Integer number,
            @RequestParam(value = "pagesize", defaultValue = "15") Integer pageSize) {
        return this.employeeService.getUsersByDepartmentId(number, pageSize, id);
    }


    @ApiOperation(
            value = "Получить всех сотрудников (между датами)",
            notes = "Получить всех сотрудников у кого день рождение между датами"
    )
    @UrlDeclaration(description = "Получить всех сотрудников у кого день рождение между датами", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @UrlDeclaration(description = "Получить всех сотрудников у кого сегодня день рождение", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDto> getUsersRangeBirthday() {
        return this.employeeService.getUserBirthdayToday();
    }


    @ApiOperation(
            value = "Получить всех сотрудников (в определенную дату)",
            notes = "Получить всех сотрудников у кого день рождение в определенную дату"
    )
    @UrlDeclaration(description = "Получить всех сотрудников у кого день рождение в определенную дату", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
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
    @UrlDeclaration(description = "Получить количество сотрудников у кого день рождение в определенную датату", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public int getCountUsersBirthday(
            final @ApiParam(value = "Дата", required = true) @PathVariable String date) throws ParseException {
        final SimpleDateFormat sdf = date.contains("-")
                ? new SimpleDateFormat("yyyy-MM-dd")
                : new SimpleDateFormat("dd.MM.yyyy");

        final Date dateParse = sdf.parse(date);
        return this.employeeService.getCountUserBirthday(dateParse);
    }

    @ApiOperation(
            value = "Получить сотрудника по инициалам"
    )
    @UrlDeclaration(description = "Получить сотрудника по имени", isAccessAll = true)
    @RequestMapping(value = "/get/by/initials/{initials}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeDto> getUsersByInitials(
            final @ApiParam(value = "Инициалы", required = true) @PathVariable String initials) {
        return this.employeeService.getUsersByInitials(initials);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @UrlDeclaration(description = "Обновить сотрудников КолАЭР из xlsx", isUser = false)
    @ApiOperation(value = "Обновить сотрудников КолАЭР из xlsx")
    public void uploadEmployee(@RequestParam("file")MultipartFile file) throws IOException {
        updateEmployeesService.updateEmployees(file.getInputStream());
    }

}
