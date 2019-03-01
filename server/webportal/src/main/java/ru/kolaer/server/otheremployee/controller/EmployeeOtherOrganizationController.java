package ru.kolaer.server.otheremployee.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.kolaer.common.dto.kolaerweb.organizations.EmployeeOtherOrganizationDto;
import ru.kolaer.server.otheremployee.service.EmployeeOtherOrganizationService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/organizations/employees")
@Api(tags = "Содрудники филиалов")
@Slf4j
public class EmployeeOtherOrganizationController {
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private final EmployeeOtherOrganizationService employeeOtherOrganizationService;

	@Autowired
	public EmployeeOtherOrganizationController(EmployeeOtherOrganizationService employeeOtherOrganizationService) {
		this.employeeOtherOrganizationService = employeeOtherOrganizationService;
	}

	@ApiOperation(
			value = "Получить всех сотрудников"
	)
	@RequestMapping(value = "/get/users/max", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganizationDto> getUsers() {
		return employeeOtherOrganizationService.getAll();
	}

	@ApiOperation(
			value = "Получить число (количество сотрудников)",
			notes = "Получить число строк общего колличества сотрудников"
	)
	@RequestMapping(value = "/get/users/сount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Long getRowCount() {
		return 1L;//employeeOtherOrganizationService.getRowCount(); TODO !!!!
	}

	@ApiOperation(
			value = "Получить сотрудников (в периоде)",
			notes = "Получить сотрудников в периоде числа"
	)
	@RequestMapping(value = "/get/users/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganizationDto> getUsersRangeBirthday(
			final @ApiParam(value = "Дата с", required = true) @PathVariable String startDate,
			final @ApiParam(value = "Дата по", required = true) @PathVariable String endDate) {
		LocalDate startDatePasre = LocalDate.parse(startDate, DATE_TIME_FORMATTER);
		LocalDate endDatePasre = LocalDate.parse(endDate, DATE_TIME_FORMATTER);
		return employeeOtherOrganizationService.getUserRangeBirthday(startDatePasre, endDatePasre);
	}

	@ApiOperation(
			value = "Получить сотрудников (сегодня)",
			notes = "Получить сотрудников у кого сегодня день рождения"
	)
	@RequestMapping(value = "/get/users/birthday/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganizationDto> getUsersRangeBirthday() {
		return employeeOtherOrganizationService.getUserBirthdayToday();
	}

	@ApiOperation(
			value = "Получить сотрудников (в указанный день)",
			notes = "Получить сотрудников у кого день рождения в указанный день"
	)
	@RequestMapping(value = "/get/users/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganizationDto> getUsersRangeBirthday(
			final @ApiParam(value = "Дата", required = true) @PathVariable String date) {
		LocalDate datePasre = LocalDate.parse(date, DATE_TIME_FORMATTER);
		return employeeOtherOrganizationService.getUsersByBirthday(datePasre);
	}

	@ApiOperation(
			value = "Получить сотрудников (в указанный день и организации)",
			notes = "Получить сотрудников у кого день рождения в указанный день из указанной организации"
	)
	@RequestMapping(value = "/get/users/{organization}/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganizationDto> getUsersRangeBirthdayAndOrg(
			final @ApiParam(value = "Дата", required = true) @PathVariable String date,
			final @ApiParam(value = "Название организации", required = true) @PathVariable String organization) {
		final SimpleDateFormat sdf = date.contains("-")
				? new SimpleDateFormat("yyyy-MM-dd")
				: new SimpleDateFormat("dd.MM.yyyy");
	    
		try {
			Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationService.getUsersByBirthdayAndOrg(datePasre, organization);
		} catch (ParseException e) {
			log.error("Ошибка! Не коректные данные: ({})", date);
			return Collections.emptyList();
		}
	}

	@ApiOperation(
			value = "Получить число сотрудников (в указанный день)",
			notes = "Получить число сотрудников у кого день рождения в указанный день"
	)
	@RequestMapping(value = "/get/users/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Integer getCountUsersBirthday(final @ApiParam(value = "Дата", required = true) @PathVariable String date) {
		LocalDate datePasre = LocalDate.parse(date, DATE_TIME_FORMATTER);
		return employeeOtherOrganizationService.getCountUserBirthday(datePasre);
	}

	@ApiOperation(
			value = "Получить число сотрудников (в указанный день и организации)",
			notes = "Получить число сотрудников у кого день рождения в указанный день из указанной организации"
	)
	@RequestMapping(value = "/get/users/{organization}/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int getCountUsersBirthday(
			final @ApiParam(value = "Дата", required = true) @PathVariable String date,
			final @ApiParam(value = "Наименование организации", required = true) @PathVariable String organization) {
		final SimpleDateFormat sdf = date.contains("-")
				? new SimpleDateFormat("yyyy-MM-dd")
				: new SimpleDateFormat("dd.MM.yyyy");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationService.getCountUserBirthdayAndOrg(datePasre, organization);
		} catch (ParseException e) {
			log.error("Ошибка! Не коректные данные: ({})", date);
			return 0;
		}
	}

	@ApiOperation(
			value = "Получить сотрудников по инициалам",
			notes = "Получить сотрудников по инициалам"
	)
	@RequestMapping(value = "/get/users/by/initials/{initials}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganizationDto> getUsersByInitials(
			final @ApiParam(value = "Иничиалы", required = true) @PathVariable String initials) {
			return employeeOtherOrganizationService.getUsersByInitials(initials);
	}
}
