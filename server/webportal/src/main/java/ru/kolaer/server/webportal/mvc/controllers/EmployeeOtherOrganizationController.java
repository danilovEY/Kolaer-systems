package ru.kolaer.server.webportal.mvc.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.api.mvp.model.restful.EmployeeOtherOrganization;
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
public class EmployeeOtherOrganizationController {
	private final Logger LOG = LoggerFactory.getLogger(EmployeeOtherOrganizationController.class);
	
	@Autowired
	protected EmployeeOtherOrganizationDao employeeOtherOrganizationDAO;

	@UrlDeclaration(description = "Получить всех сотрудников.", isAccessAll = true)
	@RequestMapping(value = "/get/users/max", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsers() {
		return employeeOtherOrganizationDAO.getAll();
	}

	@UrlDeclaration(description = "Получить ограниченное число сотрудников.", isAccessAll = true)
	@RequestMapping(value = "/get/users/max/{max}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsers(final @PathVariable int max) {
		return employeeOtherOrganizationDAO.getAllMaxCount(max);
	}

	@UrlDeclaration(description = "Получить число строк общего колличества сотрудников.", isAccessAll = true)
	@RequestMapping(value = "/get/users/сount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int getRowCount() {
		return employeeOtherOrganizationDAO.getRowCount();
	}

	@UrlDeclaration(description = "Получить сотрудников в периоде числа.", isAccessAll = true)
	@RequestMapping(value = "/get/users/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersRangeBirthday(final @PathVariable String startDate, final @PathVariable String endDate) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date startDatePasre = sdf.parse(startDate);
			final Date endDatePasre = sdf.parse(endDate);
			return employeeOtherOrganizationDAO.getUserRangeBirthday(startDatePasre, endDatePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({}, {})", startDate, endDate);
			return Collections.emptyList();
		}
	}

	@UrlDeclaration(description = "Получить сотрудников у кого сегодня день рождения.", isAccessAll = true)
	@RequestMapping(value = "/get/users/birthday/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersRangeBirthday() {
		return employeeOtherOrganizationDAO.getUserBirthdayToday();
	}

	@UrlDeclaration(description = "Получить сотрудников у кого день рождение в указанный день.", isAccessAll = true)
	@RequestMapping(value = "/get/users/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersRangeBirthday(final @PathVariable String date) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationDAO.getUsersByBirthday(datePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return Collections.emptyList();
		}
	}

	@UrlDeclaration(description = "Получить сотрудников у кого день рождение в указанный день из указанной организации.", isAccessAll = true)
	@RequestMapping(value = "/get/users/{orgainzation}/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersRangeBirthdayAndOrg(final @PathVariable String date, final @PathVariable String orgainzation) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationDAO.getUsersByBirthdayAndOrg(datePasre, orgainzation);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return Collections.emptyList();
		}
	}

	@UrlDeclaration(description = "Задать сотрудников из других организаций.", requestMethod = RequestMethod.POST)
	@RequestMapping(value = "/set/users/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public void setUsers(final @RequestBody RequestDbBirthdayAllList usersList) {
		this.employeeOtherOrganizationDAO.insertDataList(usersList.getBirthdayList());
	}

	@UrlDeclaration(description = "Получить число сотрудников у кого день рождение в указанный день.", isAccessAll = true)
	@RequestMapping(value = "/get/users/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public Integer getCountUsersBirthday(final @PathVariable String date) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationDAO.getCountUserBirthday(datePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return 0;
		}
	}

	@UrlDeclaration(description = "Получить число сотрудников у кого день рождение в указанный день из указанной организации.", isAccessAll = true)
	@RequestMapping(value = "/get/users/{organization}/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public int getCountUsersBirthday(final @PathVariable String date, final @PathVariable String organization) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return employeeOtherOrganizationDAO.getCountUserBirthdayAndOrg(datePasre, organization);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return 0;
		}
	}

	@UrlDeclaration(description = "Получить сотрудников по инициалам.", isAccessAll = true)
	@RequestMapping(value = "/get/users/by/initials/{initials}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public List<EmployeeOtherOrganization> getUsersByInitials(final @PathVariable String initials) {
			return employeeOtherOrganizationDAO.getUsersByInitials(initials);
	}
}
