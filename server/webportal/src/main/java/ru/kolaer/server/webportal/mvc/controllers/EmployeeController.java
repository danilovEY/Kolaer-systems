package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.api.mvp.model.kolaerweb.GeneralEmployeesEntity;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.servirces.EmployeeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Danilov on 27.07.2016.
 * Рест контроллер для работы с сотрудниками.
 */
@RestController
@RequestMapping(value = "/general/employees")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    private EmployeeService employeeService;

    /**Получить всех сотрудников.*/
    @UrlDeclaration(description = "Получить всех сотрудников.", isAccessAll = true)
    @RequestMapping(value = "/get/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public List<GeneralEmployeesEntity> getAllEmployees() {
        return this.employeeService.getAll();
    }

    @UrlDeclaration(description = "Получить всех сотрудников у кого день рождение между датами.", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneralEmployeesEntity> getUsersRangeBirthday(final @PathVariable String startDate, final @PathVariable String endDate) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final Date startDatePasre = sdf.parse(startDate);
        final Date endDatePasre = sdf.parse(endDate);
        return employeeService.getUserRangeBirthday(startDatePasre, endDatePasre);

    }

    @UrlDeclaration(description = "Получить всех сотрудников у кого сегодня день рождение.", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneralEmployeesEntity> getUsersRangeBirthday() {
        return this.employeeService.getUserBirthdayToday();
    }

    @UrlDeclaration(description = "Получить всех сотрудников у кого день рождение в определенную дату.", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneralEmployeesEntity> getUsersRangeBirthday(final @PathVariable String date) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final Date datePasre = sdf.parse(date);
        return this.employeeService.getUsersByBirthday(datePasre);

    }

    @UrlDeclaration(description = "Получить колличество сотрудников у кого день рождение в определенную датату.", isAccessAll = true)
    @RequestMapping(value = "/get/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public int getCountUsersBirthday(final @PathVariable String date) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        final Date datePasre = sdf.parse(date);
        return this.employeeService.getCountUserBirthday(datePasre);
    }

    @UrlDeclaration(description = "Получить сотродника по имени.", isAccessAll = true)
    @RequestMapping(value = "/get/by/initials/{initials}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<GeneralEmployeesEntity> getUsersByInitials(final @PathVariable String initials) {
        List<GeneralEmployeesEntity> result = this.employeeService.getUsersByInitials(initials);
        result.forEach(emp -> LOG.debug(emp.getDepartament().getAbbreviatedName()));
        return result;
    }
}
