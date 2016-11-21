package ru.kolaer.server.webportal.mvc.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.kolaerweb.organizations.EmployeeOtherOrganization;
import ru.kolaer.server.webportal.annotations.UrlDeclaration;
import ru.kolaer.server.webportal.mvc.model.dao.EmployeeOtherOrganizationDao;
import ru.kolaer.server.webportal.mvc.model.entities.birthday.RequestDbBirthdayAllList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/organizations/employees")
@Api(tags = "Содрудники филиалов")
public class EmployeeOtherOrganizationController {
	private final Logger LOG = LoggerFactory.getLogger(EmployeeOtherOrganizationController.class);
	
	@Autowired
	protected EmployeeOtherOrganizationDao employeeOtherOrganizationDao;

	@ApiOperation(
			value = "Получить всех сотрудников",
			notes = "Получить всех сотрудников"
	)
	@UrlDeclaration(description = "Получить всех сотрудников.", isAccessAll = true)
	@RequestMapping(value = "/get/users/max", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsers() {
		return employeeOtherOrganizationDao.getAll();
	}

	@ApiOperation(
			value = "Получить ограниченное число сотрудников",
			notes = "Получить ограниченное число сотрудников"
	)
	@UrlDeclaration(description = "Получить ограниченное число сотрудников.", isAccessAll = true)
	@RequestMapping(value = "/get/users/max/{max}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsers(final @ApiParam(value = "Ограничение", required = true) @PathVariable int max) {
		return employeeOtherOrganizationDao.getAllMaxCount(max);
	}

	@ApiOperation(
			value = "Получить число (колличество сотрудников)",
			notes = "Получить число строк общего колличества сотрудников"
	)
	@UrlDeclaration(description = "Получить число строк общего колличества сотрудников.", isAccessAll = true)
	@RequestMapping(value = "/get/users/сount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int getRowCount() {
		return employeeOtherOrganizationDao.getRowCount();
	}

	@ApiOperation(
			value = "Получить сотрудников (в периоде)",
			notes = "Получить сотрудников в периоде числа"
	)
	@UrlDeclaration(description = "Получить сотрудников в периоде числа.", isAccessAll = true)
	@RequestMapping(value = "/get/users/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersRangeBirthday(
			final @ApiParam(value = "Дата с", required = true) @PathVariable String startDate,
			final @ApiParam(value = "Дата по", required = true) @PathVariable String endDate) {
		final SimpleDateFormat sdf = startDate.contains("-")
				? new SimpleDateFormat("yyyy-MM-dd")
				: new SimpleDateFormat("dd.MM.yyyy");
	    
		try {
			final Date startDatePasre = sdf.parse(startDate);
			final Date endDatePasre = sdf.parse(endDate);
			return employeeOtherOrganizationDao.getUserRangeBirthday(startDatePasre, endDatePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({}, {})", startDate, endDate);
			return Collections.emptyList();
		}
	}

	@ApiOperation(
			value = "Получить сотрудников (сегодня)",
			notes = "Получить сотрудников у кого сегодня день рождения"
	)
	@UrlDeclaration(description = "Получить сотрудников у кого сегодня день рождения.", isAccessAll = true)
	@RequestMapping(value = "/get/users/birthday/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersRangeBirthday() {
		return employeeOtherOrganizationDao.getUserBirthdayToday();
	}

	@ApiOperation(
			value = "Получить сотрудников (в указанный день)",
			notes = "Получить сотрудников у кого день рождение в указанный день"
	)
	@UrlDeclaration(description = "Получить сотрудников у кого день рождение в указанный день.", isAccessAll = true)
	@RequestMapping(value = "/get/users/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersRangeBirthday(
			final @ApiParam(value = "Дата", required = true) @PathVariable String date) {
		final SimpleDateFormat sdf = date.contains("-")
				? new SimpleDateFormat("yyyy-MM-dd")
				: new SimpleDateFormat("dd.MM.yyyy");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationDao.getUsersByBirthday(datePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return Collections.emptyList();
		}
	}

	@ApiOperation(
			value = "Получить сотрудников (в указанный день и организации)",
			notes = "Получить сотрудников у кого день рождение в указанный день из указанной организации"
	)
	@UrlDeclaration(description = "Получить сотрудников у кого день рождение в указанный день из указанной организации.", isAccessAll = true)
	@RequestMapping(value = "/get/users/{orgainzation}/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersRangeBirthdayAndOrg(
			final @ApiParam(value = "Дата", required = true) @PathVariable String date,
			final @ApiParam(value = "Название организации", required = true) @PathVariable String orgainzation) {
		final SimpleDateFormat sdf = date.contains("-")
				? new SimpleDateFormat("yyyy-MM-dd")
				: new SimpleDateFormat("dd.MM.yyyy");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationDao.getUsersByBirthdayAndOrg(datePasre, orgainzation);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return Collections.emptyList();
		}
	}

	@ApiOperation(
			value = "Задать сотрудников из других организаций",
			notes = "Задать сотрудников из других организаций"
	)
	@UrlDeclaration(description = "Задать сотрудников из других организаций.", requestMethod = RequestMethod.POST)
	@RequestMapping(value = "/set/users/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void setUsers(final @ApiParam(value = "Список сотрудников", required = true) @RequestBody RequestDbBirthdayAllList usersList) {
		this.employeeOtherOrganizationDao.insertDataList(usersList.getBirthdayList());
	}

	@ApiOperation(
			value = "Получить число сотрудников (в указанный день)",
			notes = "Получить число сотрудников у кого день рождение в указанный день"
	)
	@UrlDeclaration(description = "Получить число сотрудников у кого день рождение в указанный день.", isAccessAll = true)
	@RequestMapping(value = "/get/users/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Integer getCountUsersBirthday(final @ApiParam(value = "Дата", required = true) @PathVariable String date) {
		final SimpleDateFormat sdf = date.contains("-")
				? new SimpleDateFormat("yyyy-MM-dd")
				: new SimpleDateFormat("dd.MM.yyyy");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationDao.getCountUserBirthday(datePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return 0;
		}
	}

	@ApiOperation(
			value = "Получить число сотрудников (в указанный день и организации)",
			notes = "Получить число сотрудников у кого день рождение в указанный день из указанной организации"
	)
	@UrlDeclaration(description = "Получить число сотрудников у кого день рождение в указанный день из указанной организации.", isAccessAll = true)
	@RequestMapping(value = "/get/users/{organization}/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int getCountUsersBirthday(
			final @ApiParam(value = "Дата", required = true) @PathVariable String date,
			final @ApiParam(value = "Наименование организации", required = true) @PathVariable String organization) {
		final SimpleDateFormat sdf = date.contains("-")
				? new SimpleDateFormat("yyyy-MM-dd")
				: new SimpleDateFormat("dd.MM.yyyy");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationDao.getCountUserBirthdayAndOrg(datePasre, organization);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return 0;
		}
	}

	@ApiOperation(
			value = "Получить сотрудников по инициалам",
			notes = "Получить сотрудников по инициалам"
	)
	@UrlDeclaration(description = "Получить сотрудников по инициалам.", isAccessAll = true)
	@RequestMapping(value = "/get/users/by/initials/{initials}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersByInitials(
			final @ApiParam(value = "Иничиалы", required = true) @PathVariable String initials) {
			return employeeOtherOrganizationDao.getUsersByInitials(initials);
	}
}
