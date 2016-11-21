package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dao.impl.DataBaseInitialization;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 * Рест контроллер для работы с сотрудниками.
 */
@RestController
@RequestMapping(value = "/general/employees")
@Api(tags = "Сотрудники КолАЭР", description = "Сотрудники из организации КолАЭР.")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DataBaseInitialization dataBaseInitialization;


    @ApiOperation(
            value = "Получить всех сотрудников",
            notes = "Получить всех сотрудников"
    )
    @UrlDeclaration(description = "Получить всех сотрудников.", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<GeneralEmployeesEntity> getAllEmployees() {
        return this.employeeService.getAll();
    }

    @ApiOperation(
            value = "Получить всех сотрудников у кого день рождение между датами",
            notes = "Получить всех сотрудников у кого день рождение между датами"
    )
    @UrlDeclaration(description = "Получить всех сотрудников у кого день рождение между датами.", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneralEmployeesEntity> getUsersRangeBirthday(
            final @ApiParam(value = "Дата с", required = true) @PathVariable String startDate,
            final @ApiParam(value = "Дата по", required = true) @PathVariable String endDate) throws ParseException {
        final SimpleDateFormat sdf = startDate.contains("-")
                ? new SimpleDateFormat("yyyy-MM-dd")
                : new SimpleDateFormat("dd.MM.yyyy");

        final Date startdateParse = sdf.parse(startDate);
        final Date enddateParse = sdf.parse(endDate);
        return employeeService.getUserRangeBirthday(startdateParse, enddateParse);
    }

    @ApiOperation(
            value = "Получить всех сотрудников у кого сегодня день рождение",
            notes = "Получить всех сотрудников у кого сегодня день рождение"
    )
    @UrlDeclaration(description = "Получить всех сотрудников у кого сегодня день рождение.", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneralEmployeesEntity> getUsersRangeBirthday() {
        return this.employeeService.getUserBirthdayToday();
    }


    @ApiOperation(
            value = "Получить всех сотрудников у кого день рождение в определенную дату",
            notes = "Получить всех сотрудников у кого день рождение в определенную дату"
    )
    @UrlDeclaration(description = "Получить всех сотрудников у кого день рождение в определенную дату.", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneralEmployeesEntity> getUsersRangeBirthday(
           final @ApiParam(value = "Дата", required = true) @PathVariable String date) throws ParseException {
        final SimpleDateFormat sdf = date.contains("-")
                ? new SimpleDateFormat("yyyy-MM-dd")
                : new SimpleDateFormat("dd.MM.yyyy");

        final Date dateParse = sdf.parse(date);
        return this.employeeService.getUsersByBirthday(dateParse);
    }

    @ApiOperation(
            value = "Получить колличество сотрудников у кого день рождение в определенную датату",
            notes = "Получить колличество сотрудников у кого день рождение в определенную датату"
    )
    @UrlDeclaration(description = "Получить колличество сотрудников у кого день рождение в определенную датату.", isAccessAll = true)
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
            value = "Получить сотрудника по инициалам",
            notes = "Получить сотрудника по инициалам"
    )
    @UrlDeclaration(description = "Получить сотрудника по имени.", isAccessAll = true)
    @RequestMapping(value = "/get/by/initials/{initials}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneralEmployeesEntity> getUsersByInitials(
            final @ApiParam(value = "Инициалы", required = true) @PathVariable String initials) {
        List<GeneralEmployeesEntity> result = this.employeeService.getUsersByInitials(initials);
        return result;
    }

    @ApiOperation(
            value = "Обновить бвзу сотрудников из kolaer_base.db_date_all",
            notes = "Обновить бвзу сотрудников из kolaer_base.db_date_all"
    )
    @UrlDeclaration(description = "Обновить бвзу сотрудников из kolaer_base.")
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void updateEmployee() {
        this.dataBaseInitialization.updateDataBase();
    }
}
