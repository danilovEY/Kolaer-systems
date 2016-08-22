package ru.kolaer.server.restful.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.kolaer.server.dao.DbBirthdayAllDAO;
import ru.kolaer.server.dao.entities.DbBirthdayAll;
import ru.kolaer.server.restful.controller.request.obj.RequestDbBirthdayAllList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value="/database/birthdayAll")
public class DataBaseBirthdayAllController {
	private final Logger LOG = LoggerFactory.getLogger(DataBaseBirthdayAllController.class);
	
	@Autowired
	protected DbBirthdayAllDAO dbBirthdayAllDAO;
	
	@RequestMapping(value = "/get/users/max", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbBirthdayAll> getUsers() {
		return dbBirthdayAllDAO.getAll();
	}
	
	@RequestMapping(value = "/get/users/max/{max}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbBirthdayAll> getUsers(final @PathVariable int max) {
		return dbBirthdayAllDAO.getAllMaxCount(max);
	}

	@RequestMapping(value = "/get/users/rowCount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getRowCount() {
		return dbBirthdayAllDAO.getRowCount();
	}
	
	@RequestMapping(value = "/get/users/birthday/{startDate}/{endDate}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbBirthdayAll> getUsersRangeBirthday(final @PathVariable String startDate, final @PathVariable String endDate) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date startDatePasre = sdf.parse(startDate);
			final Date endDatePasre = sdf.parse(endDate);
			return dbBirthdayAllDAO.getUserRangeBirthday(startDatePasre, endDatePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({}, {})", startDate, endDate);
			return Collections.emptyList();
		}
	}	
	
	@RequestMapping(value = "/get/users/birthday/today", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbBirthdayAll> getUsersRangeBirthday() {
		return dbBirthdayAllDAO.getUserBirthdayToday();
	}
	
	@RequestMapping(value = "/get/users/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbBirthdayAll> getUsersRangeBirthday(final @PathVariable String date) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return dbBirthdayAllDAO.getUsersByBirthday(datePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return Collections.emptyList();
		}
	}	
	
	@RequestMapping(value = "/get/users/{orgainzation}/birthday/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbBirthdayAll> getUsersRangeBirthdayAndOrg(final @PathVariable String date, final @PathVariable String orgainzation) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return dbBirthdayAllDAO.getUsersByBirthdayAndOrg(datePasre, orgainzation);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return Collections.emptyList();
		}
	}	
	
	@RequestMapping(value = "/set/users/list", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public void setUsers(final @RequestBody RequestDbBirthdayAllList usersList) {
		this.dbBirthdayAllDAO.insertDataList(usersList.getBirthdayList());
	}	
	
	@RequestMapping(value = "/get/users/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Integer getCountUsersBirsday(final @PathVariable String date) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return dbBirthdayAllDAO.getCountUserBirthday(datePasre);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return 0;
		}
	}
	
	@RequestMapping(value = "/get/users/{orgainzation}/birthday/{date}/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public int getCountUsersBirsday(final @PathVariable String date, final @PathVariable String orgainzation) {
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    
		try {
			final Date datePasre = sdf.parse(date);
			return dbBirthdayAllDAO.getCountUserBirthdayAndOrg(datePasre, orgainzation);
		} catch (ParseException e) {
			LOG.error("Ошибка! Не коректные данные: ({})", date);
			return 0;
		}
	}
	
	@RequestMapping(value = "/get/users/by/initials/{initials}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<DbBirthdayAll> getUsersByInitials(final @PathVariable String initials) {
			return dbBirthdayAllDAO.getUsersByInitials(initials);
	}
}
